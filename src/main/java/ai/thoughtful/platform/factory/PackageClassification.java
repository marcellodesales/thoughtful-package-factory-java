package ai.thoughtful.platform.factory;

import ai.thoughtful.platform.factory.model.Package;
import ai.thoughtful.platform.factory.model.PackageDimension;

import java.util.EnumSet;
import java.util.Set;

public enum PackageClassification {
    // A package is bulky if its volume (Width x Height x Length) is greater than or equal to 1,000,000 cm³
    // when one of its dimensions is greater or equal to 150 cm.
    BULKY,

    // when its mass is greater or equal to 20 kg.
    HEAVY;

    private static final int BULKY_LIMIT = 1000000;
    private static final int HEAVY_MASS_LIMIT = 20 * 1000;

    public static EnumSet<PackageClassification> classify(Package packageUnit) {
        PackageDimension dimension = packageUnit.dimension();
        Set<PackageClassification> classifications = EnumSet.noneOf(PackageClassification.class);
        if (dimension.getVolume() >= BULKY_LIMIT ||
                dimension.length() >= 150 || dimension.height() >= 150 || dimension.width() >= 150) {
            classifications.add(BULKY);
        }

        if (packageUnit.mass() >= HEAVY_MASS_LIMIT) {
            classifications.add(HEAVY);
        }

        return EnumSet.copyOf(classifications);
    }
}
