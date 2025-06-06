package ai.thoughtful.platform.factory.model;

import com.google.common.base.Preconditions;

public record Package(
        // The dimension of the package
        PackageDimension dimension,
        // The mass of the package
        double mass
) {

    public Package {
        Preconditions.checkNotNull(dimension,
                "Can't create a package without dimension");
        Preconditions.checkArgument(mass > 0 && Double.isFinite(mass),
                "Can't create package with invalid finite mass number");
    }
}
