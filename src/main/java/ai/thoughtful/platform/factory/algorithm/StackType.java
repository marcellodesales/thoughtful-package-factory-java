package ai.thoughtful.platform.factory.algorithm;

import ai.thoughtful.platform.factory.model.Package;
import ai.thoughtful.platform.factory.model.PackageFactory;

import java.util.EnumSet;

public enum StackType {

    // standard packages (those that are not bulky or heavy) can be handled normally.
    STANDARD,
    // packages that are either heavy or bulky can't be handled automatically
    SPECIAL,
    // packages that are **both** heavy and bulky are rejected.
    REJECTED;

    public static StackType sort(Package pkg) {
        EnumSet<PackageClassificationType> pkgClassification = PackageClassificationType.classify(pkg);

        // If there's a single classification, then it's special (either heavy or bulky)
        if (pkgClassification.size() == 1) {
            return StackType.SPECIAL;

        } else if (pkgClassification.size() > 1) {
            // If it's bulky and heavy, then just reject
            EnumSet<PackageClassificationType> rejectSet = EnumSet.of(PackageClassificationType.BULKY, PackageClassificationType.HEAVY);
            if (rejectSet.containsAll(pkgClassification)) {
                return StackType.REJECTED;
            }
        }

        // Since the set is empty, there's no classification
        return STANDARD;
    }

    public static String sort(int width, int height, int length, double mass) {
        Package pkg = PackageFactory.make(width, height, length, mass);
        return StackType.sort(pkg).name();
    }
}
