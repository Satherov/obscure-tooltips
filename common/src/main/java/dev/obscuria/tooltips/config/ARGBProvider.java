package dev.obscuria.tooltips.config;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.util.color.ARGB;

public interface ARGBProvider {

    Codec<ARGBProvider> CODEC = Codec
            .either(ARGBDelegate.CODEC, ARGB.CODEC)
            .xmap(ARGBProvider::fromEither, ARGBProvider::asEither);

    Either<ARGBDelegate, ARGB> asEither();

    ARGB get();

    static ARGBProvider fromEither(Either<ARGBDelegate, ARGB> either) {
        return either.map(Config::new, Literal::new);
    }

    record Literal(ARGB color) implements ARGBProvider {

        @Override
        public Either<ARGBDelegate, ARGB> asEither() {
            return Either.right(color);
        }

        @Override
        public ARGB get() {
            return color;
        }
    }

    record Config(ARGBDelegate delegate) implements ARGBProvider {

        @Override
        public Either<ARGBDelegate, ARGB> asEither() {
            return Either.left(delegate);
        }

        @Override
        public ARGB get() {
            return delegate.color();
        }
    }
}
