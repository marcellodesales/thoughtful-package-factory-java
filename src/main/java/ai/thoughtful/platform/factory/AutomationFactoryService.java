package ai.thoughtful.platform.factory;

import ai.thoughtful.platform.factory.model.Package;
import ai.thoughtful.platform.factory.model.PackageFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;

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

    public EnumSet<PackageClassification> classifyPackage(Package pkg) {
        return PackageClassification.classify(pkg);
    }
}
