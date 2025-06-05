package ai.thoughtful.platform.factory.model;

import com.google.common.base.Preconditions;

public record PackageDimension(
        int height,
        int width,
        int length
) {
    public PackageDimension {
        Preconditions.checkArgument(width > 0 && height > 0 && length > 0,
                "Can't create package with invalid dimensions");
    }
    public int getVolume() {
        return width * length * height;
    }
}
