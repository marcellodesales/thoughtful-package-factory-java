package ai.thoughtful.platform.factory.model;

public class PackageFactory {

    public static ai.thoughtful.platform.factory.model.Package make(int width, int height, int length, double mass) {
        PackageDimension dimension = new PackageDimension(height, width, length);
        return new Package(dimension, mass);
    }
}

