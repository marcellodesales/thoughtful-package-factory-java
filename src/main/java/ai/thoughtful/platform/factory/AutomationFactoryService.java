package ai.thoughtful.platform.factory;

import ai.thoughtful.platform.factory.algorithm.PackageClassificationType;
import ai.thoughtful.platform.factory.algorithm.StackType;
import ai.thoughtful.platform.factory.model.Package;
import ai.thoughtful.platform.factory.model.PackageDimension;
import ai.thoughtful.platform.factory.model.PackageFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AutomationFactoryService {

    public Package makeNewPackage(int width, int height, int length, double mass) {
        return PackageFactory.builder()
                .withWidthInCm(width)
                .withHeightInCm(height)
                .withLengthInCm(length)
                .withMassInGrams(mass).build();
    }
    public StackType sortPackage(Package pkg) {
        return StackType.sort(pkg);
    }

    public StackType sortPackage(int width, int height, int length, double mass) {
        Package pkg = PackageFactory.make(width, height, length, mass);
        return StackType.sort(pkg);
    }

    public EnumSet<PackageClassificationType> classifyPackage(Package pkg) {
        return PackageClassificationType.classify(pkg);
    }

    /**
     * @param packageInfo the instance to return.
     * @return The remarks about the sort and classification
     */
    public Set<String> getPackageRemarks(Package packageInfo, EnumSet<PackageClassificationType> classification) {
        Set<String> remarks = new HashSet<>(this.getClassificationReason(packageInfo, classification));
        remarks.addAll(this.getSortReason(packageInfo, classification));
        return remarks;
    }

    /**
     * @param packageInfo a package instance.
     * @return The set of bulky reasons
     */
    public Set<String> getClassificationReason(Package packageInfo, EnumSet<PackageClassificationType> classification) {
        PackageDimension dimension = packageInfo.dimension();
        Set<String> reasons = new HashSet<>();
        for (PackageClassificationType classificationType : classification) {
            if (classificationType == PackageClassificationType.BULKY) {
                List<Integer> limits = Arrays.asList(dimension.width(), dimension.height(), dimension.length());
                if (limits.stream().anyMatch(dim -> dim >= PackageClassificationType.DIMENTION_LIMIT)) {
                    reasons.add("Dimension >= %dcm".formatted(PackageClassificationType.DIMENTION_LIMIT));
                }
                if (dimension.getVolume() >= PackageClassificationType.BULKY_LIMIT) {
                    reasons.add("Volume >= %dcm³".formatted(PackageClassificationType.BULKY_LIMIT));
                }
            }
            if (classificationType == PackageClassificationType.HEAVY) {
                reasons.add("Weight >= %d".formatted(PackageClassificationType.HEAVY_MASS_LIMIT));
            }
        }
        if (reasons.isEmpty()) {
            reasons.add("Not bulky nor heavy");
        }
        return reasons;
    }

    /**
     * @param packageInfo the package provided
     * @param packageClassification the classification of the package.
     * @return The classification reasons.
     */
    public Set<String> getSortReason(Package packageInfo, EnumSet<PackageClassificationType> packageClassification) {
        Set<String> classificationReasons = new HashSet<>();

        if (packageClassification.containsAll(EnumSet.of(PackageClassificationType.BULKY, PackageClassificationType.HEAVY))) {
            classificationReasons.add("BULKY and HEAVY");

        } else if (packageClassification.containsAll(EnumSet.of(PackageClassificationType.BULKY))) {
            classificationReasons.add("BULKY only");

        } else if (packageClassification.containsAll(EnumSet.of(PackageClassificationType.HEAVY))) {
            classificationReasons.add("HEAVY only");

        } else {
            classificationReasons.add("STANDARD");
        }
        return classificationReasons;
    }
}
