package org.workcraft.plugins.shared;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import org.workcraft.Config;
import org.workcraft.dom.visual.Positioning;
import org.workcraft.gui.propertyeditor.PropertyDeclaration;
import org.workcraft.gui.propertyeditor.PropertyDescriptor;
import org.workcraft.gui.propertyeditor.Settings;

public class CommonVisualSettings implements Settings {
    private static final LinkedList<PropertyDescriptor> properties = new LinkedList<>();
    private static final String prefix = "CommonVisualSettings";

    private static final String keyFontSize = prefix + ".fontSize";
    private static final String keyNodeSize = prefix + ".nodeSize";
    private static final String keyStrokeWidth = prefix + ".strokeWidth";
    private static final String keyBorderColor = prefix + ".borderColor";
    private static final String keyFillColor = prefix + ".fillColor";
    private static final String keyPivotSize = prefix + ".pivotSize";
    private static final String keyPivotWidth = prefix + ".pivotWidth";
    private static final String keyLineSpacing = prefix + ".lineSpacing";
    private static final String keyLabelVisibility = prefix + ".labelVisibility";
    private static final String keyLabelPositioning = prefix + ".labelPositioning";
    private static final String keyLabelColor = prefix + ".labelColor";
    private static final String keyNameVisibility = prefix + ".nameVisibility";
    private static final String keyNamePositioning = prefix + ".namePositioning";
    private static final String keyNameColor = prefix + ".nameColor";
    private static final String keyUseSubscript = prefix + ".useSubscript";

    private static final double defaultFontSize = 10.0;
    private static final double defaultNodeSize = 1.0;
    private static final double defaultStrokeWidth = 0.1;
    private static final Color defaultBorderColor = Color.BLACK;
    private static final Color defaultFillColor = Color.WHITE;
    private static final Double defaultPivotSize = 0.2;
    private static final Double defaultPivotWidth = 0.02;
    private static final double defaultLineSpacing = 0.3;
    private static final boolean defaultLabelVisibility = true;
    private static final Positioning defaultLabelPositioning = Positioning.TOP;
    private static final Color defaultLabelColor = Color.BLACK;
    private static final boolean defaultNameVisibility = true;
    private static final Positioning defaultNamePositioning = Positioning.BOTTOM;
    private static final Color defaultNameColor = Color.GRAY.darker();
    private static final boolean defaultUseSubscript = false;

    private static double fontSize = defaultFontSize;
    private static double nodeSize = defaultNodeSize;
    private static double strokeWidth = defaultStrokeWidth;
    private static Color borderColor = defaultBorderColor;
    private static Color fillColor = defaultFillColor;
    private static Double pivotSize = defaultPivotSize;
    private static Double pivotWidth = defaultPivotWidth;
    private static double lineSpacing = defaultLineSpacing;
    private static boolean labelVisibility = defaultLabelVisibility;
    private static Positioning labelPositioning = defaultLabelPositioning;
    private static Color labelColor = defaultLabelColor;
    private static boolean nameVisibility = defaultNameVisibility;
    private static Positioning namePositioning = defaultNamePositioning;
    private static Color nameColor = defaultNameColor;
    private static boolean useSubscript = defaultUseSubscript;

    public CommonVisualSettings() {
        properties.add(new PropertyDeclaration<CommonVisualSettings, Double>(
                this, "Base font size (point) - requires restart", Double.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Double value) {
                setFontSize(value);
            }
            protected Double getter(CommonVisualSettings object) {
                return getFontSize();
            }
        });

        properties.add(new PropertyDeclaration<CommonVisualSettings, Double>(
                this, "Node size (cm)", Double.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Double value) {
                setNodeSize(value);
            }
            protected Double getter(CommonVisualSettings object) {
                return getNodeSize();
            }
        });

        properties.add(new PropertyDeclaration<CommonVisualSettings, Double>(
                this, "Stroke width (cm)", Double.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Double value) {
                setStrokeWidth(value);
            }
            protected Double getter(CommonVisualSettings object) {
                return getStrokeWidth();
            }
        });

        properties.add(new PropertyDeclaration<CommonVisualSettings, Color>(
                this, "Border color", Color.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Color value) {
                setBorderColor(value);
            }
            protected Color getter(CommonVisualSettings object) {
                return getBorderColor();
            }
        });

        properties.add(new PropertyDeclaration<CommonVisualSettings, Color>(
                this, "Fill color", Color.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Color value) {
                setFillColor(value);
            }
            protected Color getter(CommonVisualSettings object) {
                return getFillColor();
            }
        });

        properties.add(new PropertyDeclaration<CommonVisualSettings, Double>(
                this, "Pivot size (cm)", Double.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Double value) {
                setPivotSize(value);
            }
            protected Double getter(CommonVisualSettings object) {
                return getPivotSize();
            }
        });

        properties.add(new PropertyDeclaration<CommonVisualSettings, Double>(
                this, "Pivot stroke width (cm)", Double.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Double value) {
                setPivotWidth(value);
            }
            protected Double getter(CommonVisualSettings object) {
                return getPivotWidth();
            }
        });

        properties.add(new PropertyDeclaration<CommonVisualSettings, Double>(
                this, "Line spacing in multi-line text (ratio)", Double.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Double value) {
                setLineSpacing(value);
            }
            protected Double getter(CommonVisualSettings object) {
                return getLineSpacing();
            }
        });

        properties.add(new PropertyDeclaration<CommonVisualSettings, Boolean>(
                this, "Show component labels", Boolean.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Boolean value) {
                setLabelVisibility(value);
            }
            protected Boolean getter(CommonVisualSettings object) {
                return getLabelVisibility();
            }
        });

        properties.add(new PropertyDeclaration<CommonVisualSettings, Positioning>(
                this, "Label positioning", Positioning.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Positioning value) {
                setLabelPositioning(value);
            }
            protected Positioning getter(CommonVisualSettings object) {
                return getLabelPositioning();
            }
        });

        properties.add(new PropertyDeclaration<CommonVisualSettings, Color>(
                this, "Label color", Color.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Color value) {
                setLabelColor(value);
            }
            protected Color getter(CommonVisualSettings object) {
                return getLabelColor();
            }
        });

        properties.add(new PropertyDeclaration<CommonVisualSettings, Boolean>(
                this, "Show component names", Boolean.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Boolean value) {
                setNameVisibility(value);
            }
            protected Boolean getter(CommonVisualSettings object) {
                return getNameVisibility();
            }
        });

        properties.add(new PropertyDeclaration<CommonVisualSettings, Positioning>(
                this, "Name positioning", Positioning.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Positioning value) {
                setNamePositioning(value);
            }
            protected Positioning getter(CommonVisualSettings object) {
                return getNamePositioning();
            }
        });

        properties.add(new PropertyDeclaration<CommonVisualSettings, Color>(
                this, "Name color", Color.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Color value) {
                setNameColor(value);
            }
            protected Color getter(CommonVisualSettings object) {
                return getNameColor();
            }
        });

        properties.add(new PropertyDeclaration<CommonVisualSettings, Boolean>(
                this, "In Boolean expressions render text after \'_\' as subscript", Boolean.class, true, false, false) {
            protected void setter(CommonVisualSettings object, Boolean value) {
                setUseSubscript(value);
            }
            protected Boolean getter(CommonVisualSettings object) {
                return getUseSubscript();
            }
        });
    }

    @Override
    public List<PropertyDescriptor> getDescriptors() {
        return properties;
    }

    @Override
    public void load(Config config) {
        setFontSize(config.getDouble(keyFontSize, defaultFontSize));
        setNodeSize(config.getDouble(keyNodeSize, defaultNodeSize));
        setStrokeWidth(config.getDouble(keyStrokeWidth, defaultStrokeWidth));
        setBorderColor(config.getColor(keyBorderColor, defaultBorderColor));
        setFillColor(config.getColor(keyFillColor, defaultFillColor));
        setPivotSize(config.getDouble(keyPivotSize, defaultPivotSize));
        setPivotWidth(config.getDouble(keyPivotWidth, defaultPivotWidth));
        setLineSpacing(config.getDouble(keyLineSpacing, defaultLineSpacing));
        setLabelVisibility(config.getBoolean(keyLabelVisibility, defaultLabelVisibility));
        setLabelPositioning(config.getTextPositioning(keyLabelPositioning, defaultLabelPositioning));
        setLabelColor(config.getColor(keyLabelColor, defaultLabelColor));
        setNameVisibility(config.getBoolean(keyNameVisibility, defaultNameVisibility));
        setNamePositioning(config.getTextPositioning(keyNamePositioning, defaultNamePositioning));
        setNameColor(config.getColor(keyNameColor, defaultNameColor));
        setUseSubscript(config.getBoolean(keyUseSubscript, defaultUseSubscript));
    }

    @Override
    public void save(Config config) {
        config.setDouble(keyFontSize, getFontSize());
        config.setDouble(keyNodeSize, getNodeSize());
        config.setDouble(keyStrokeWidth, getStrokeWidth());
        config.setColor(keyBorderColor, getBorderColor());
        config.setColor(keyFillColor, getFillColor());
        config.setDouble(keyPivotSize, getPivotSize());
        config.setDouble(keyPivotWidth, getPivotWidth());
        config.setDouble(keyLineSpacing, getLineSpacing());
        config.setBoolean(keyLabelVisibility, getLabelVisibility());
        config.setTextPositioning(keyLabelPositioning, getLabelPositioning());
        config.setColor(keyLabelColor, getLabelColor());
        config.setBoolean(keyNameVisibility, getNameVisibility());
        config.setColor(keyNameColor, getNameColor());
        config.setTextPositioning(keyNamePositioning, getNamePositioning());
        config.setBoolean(keyUseSubscript, getUseSubscript());
    }

    @Override
    public String getSection() {
        return "Common";
    }

    @Override
    public String getName() {
        return "Visual";
    }

    public static double getFontSize() {
        return fontSize;
    }

    public static void setFontSize(double value) {
        fontSize = value;
    }

    public static double getNodeSize() {
        return nodeSize;
    }

    public static void setNodeSize(double value) {
        nodeSize = value;
    }

    public static double getStrokeWidth() {
        return strokeWidth;
    }

    public static void setStrokeWidth(double value) {
        strokeWidth = value;
    }

    public static Color getBorderColor() {
        return borderColor;
    }

    public static void setBorderColor(Color value) {
        borderColor = value;
    }

    public static Color getFillColor() {
        return fillColor;
    }

    public static void setFillColor(Color value) {
        fillColor = value;
    }

    public static double getPivotSize() {
        return pivotSize;
    }

    public static void setPivotSize(double value) {
        pivotSize = value;
    }

    public static double getPivotWidth() {
        return pivotWidth;
    }

    public static void setPivotWidth(double value) {
        pivotWidth = value;
    }

    public static double getLineSpacing() {
        return lineSpacing;
    }

    public static void setLineSpacing(double value) {
        lineSpacing = value;
    }

    public static Boolean getLabelVisibility() {
        return labelVisibility;
    }

    public static void setLabelVisibility(Boolean value) {
        labelVisibility = value;
    }

    public static Positioning getLabelPositioning() {
        return labelPositioning;
    }

    public static void setLabelPositioning(Positioning value) {
        labelPositioning = value;
    }

    public static Color getLabelColor() {
        return labelColor;
    }

    public static void setLabelColor(Color value) {
        labelColor = value;
    }

    public static Boolean getNameVisibility() {
        return nameVisibility;
    }

    public static void setNameVisibility(Boolean value) {
        nameVisibility = value;
    }

    public static Positioning getNamePositioning() {
        return namePositioning;
    }

    public static void setNamePositioning(Positioning value) {
        namePositioning = value;
    }

    public static Color getNameColor() {
        return nameColor;
    }

    public static void setNameColor(Color value) {
        nameColor = value;
    }

    public static boolean getUseSubscript() {
        return useSubscript;
    }

    public static void setUseSubscript(boolean value) {
        useSubscript = value;
    }

}
