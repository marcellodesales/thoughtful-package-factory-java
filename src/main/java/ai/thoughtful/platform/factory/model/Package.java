package ai.thoughtful.platform.factory.model;

import ai.thoughtful.platform.factory.PackageClassification;
import com.google.common.base.Preconditions;

public record Package(
        // The dimension of the package
        PackageDimension dimension,
        // The mass of the package
        double mass
) {

    public Package {
        Preconditions.checkArgument(mass > 0,
                "Can't create package with invalid mass");
    }
}
