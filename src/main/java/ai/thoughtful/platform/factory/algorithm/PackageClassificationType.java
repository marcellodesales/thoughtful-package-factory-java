package ai.thoughtful.platform.factory.algorithm;

import ai.thoughtful.platform.factory.model.Package;
import ai.thoughtful.platform.factory.model.PackageDimension;

import java.util.EnumSet;
import java.util.Set;

public enum PackageClassificationType {
    // A package is bulky if its volume (Width x Height x Length) is greater than or equal to 1,000,000 cm³
    // when one of its dimensions is greater or equal to 150 cm.
    BULKY,

    // when its mass is greater or equal to 20 kg.
    HEAVY;

    public static final int BULKY_LIMIT = 1_000_000;
    public static final int HEAVY_MASS_LIMIT = 20_000;
    public static final int DIMENTION_LIMIT = 150;

    public static EnumSet<PackageClassificationType> classify(Package packageUnit) {
        PackageDimension dimension = packageUnit.dimension();
        EnumSet<PackageClassificationType> classifications = EnumSet.noneOf(PackageClassificationType.class);
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
