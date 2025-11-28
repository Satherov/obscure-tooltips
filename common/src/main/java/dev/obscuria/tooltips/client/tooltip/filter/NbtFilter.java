package dev.obscuria.tooltips.client.tooltip.filter;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record NbtFilter(
        CompoundTag nbt,
        boolean matchExact
) implements ItemFilter {

    public static final MapCodec<NbtFilter> CODEC;

    @Override
    public MapCodec<NbtFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        assert Minecraft.getInstance().level != null;
        final var registryAccess = Minecraft.getInstance().level.registryAccess();
        final var savedItem = (CompoundTag) stack.save(registryAccess);
        final var components = savedItem.getCompound("Components");
        return matchExact
                ? NbtUtils.compareNbt(nbt, components, true)
                : isSubtagOf(nbt, components);
    }

    private static DataResult<CompoundTag> tryParseNbt(String input) {
        try {
            return DataResult.success(TagParser.parseTag(input));
        } catch (CommandSyntaxException exception) {
            return DataResult.error(() -> "Invalid tag: " + exception.getMessage());
        }
    }

    private static boolean isSubtagOf(@Nullable Tag tag, @Nullable Tag parent) {
        if (tag == null) return true;
        if (parent == null) return false;
        if (tag.getId() != parent.getId()) return false;

        if (tag instanceof CompoundTag compound1 && parent instanceof CompoundTag compound2) {
            for (var key : compound1.getAllKeys()) {
                final @Nullable var sub1 = compound1.get(key);
                final @Nullable var sub2 = compound2.get(key);
                if (!isSubtagOf(sub1, sub2)) return false;
            }
            return true;
        }

        if (tag instanceof ListTag list1 && parent instanceof ListTag list2) {
            if (list1.size() > list2.size()) return false;
            for (var element1 : list1) {
                var found = false;
                for (var element2 : list2) {
                    if (!isSubtagOf(element1, element2)) continue;
                    found = true;
                    break;
                }
                if (!found) return false;
            }
            return true;
        }

        return tag.equals(parent);
    }

    static {
        final var tagCodec = Codec.STRING.comapFlatMap(NbtFilter::tryParseNbt, CompoundTag::toString);
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                tagCodec.fieldOf("nbt").forGetter(NbtFilter::nbt),
                Codec.BOOL.optionalFieldOf("match_exact", false).forGetter(NbtFilter::matchExact)
        ).apply(codec, NbtFilter::new));
    }
}
