package cat.jiu.core.util.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {
    public static String getSize(double size) {
        if (size < 1024) {
            return String.format("%.2f Byte", size);
        }
        size /= 1024;
        if (size < 1024) {
            return String.format("%.2f KB", size);
        }
        size /= 1024;
        if (size < 1024) {
            return String.format("%.2f MB", size);
        }
        size /= 1024;
        if (size < 1024) {
            return String.format("%.2f GB", size);
        }
        size /= 1024;
        if (size < 1024) {
            return String.format("%.2f TB", size);
        }
        size /= 1024;
        if (size < 1024) {
            return String.format("%.2f PB", size);
        }
        size /= 1024;
        if (size < 1024) {
            return String.format("%.2f EB", size);
        }
        size /= 1024;
        if (size < 1024) {
            return String.format("%.2f ZB", size);
        }
        size /= 1024;
        if (size < 1024) {
            return String.format("%.2f YB", size);
        }
        return formatInternationalNumber(String.format("%.2f", size)) + "K YB";
    }

    /**
     * International: 1k( 1,000 ).
     */
    public static String formatInternationalNumber(long num) {
        return formatNumber(num, 3);
    }
    /**
     * International: 1k( 1,000 ).
     */
    public static String formatInternationalNumber(String num) {
        return formatNumber(num, 3);
    }
    /**
     * International: 10k( 10,000 ). Chinese: 1w( 1,0000 )
     */
    public static String formatChineseNumber(long num) {
        return formatNumber(num, 4);
    }
    /**
     * International: 10k( 10,000 ). Chinese: 1w( 1,0000 )
     */
    public static String formatChineseNumber(String num) {
        return formatNumber(num, 4);
    }
    public static String formatNumber(long number, int digits) {
        return formatNumber(String.valueOf(number), digits);
    }
    public static String formatNumber(String number, int digits) {
        if (digits <= 0) {
            digits = 3;
        }
        return number.replaceAll("(\\d)(?=(\\d{" + digits + "})+$)", "$1,");
    }

    public static MutableComponent copyOnClickedText(Component component) {
        return copyOnClickedText(component, component.getString());
    }
    public static MutableComponent copyOnClickedText(Component component, String copy) {
        return copyOnClickedText(component, copy, CommonComponents.GUI_COPY_LINK_TO_CLIPBOARD);
    }
    public static MutableComponent copyOnClickedText(Component component, String copy, Component hoverText) {
        return copyOnClickedText(component, copy, hoverText, ChatFormatting.GREEN);
    }
    public static MutableComponent copyOnClickedText(Component component, String copy, Component hoverText, ChatFormatting textColor) {
        return eventOnClickedText(component, ClickEvent.Action.COPY_TO_CLIPBOARD, copy, HoverEvent.Action.SHOW_TEXT, hoverText, textColor);
    }

    public static MutableComponent suggestCommandOnClickedText(Component component) {
        return suggestCommandOnClickedText(component, component.getString());
    }
    public static MutableComponent suggestCommandOnClickedText(Component component, String command) {
        return suggestCommandOnClickedText(component, command, CommonComponents.GUI_COPY_LINK_TO_CLIPBOARD);
    }
    public static MutableComponent suggestCommandOnClickedText(Component component, String command, Component hoverText) {
        return suggestCommandOnClickedText(component, command, hoverText, ChatFormatting.GREEN);
    }
    public static MutableComponent suggestCommandOnClickedText(Component component, String command, Component hoverText, ChatFormatting textColor) {
        return eventOnClickedText(component, ClickEvent.Action.SUGGEST_COMMAND, command, HoverEvent.Action.SHOW_TEXT, hoverText, textColor);
    }

    public static MutableComponent openFileOnClickedText(Component component) {
        return openFileOnClickedText(component, component.getString());
    }
    public static MutableComponent openFileOnClickedText(Component component, String file) {
        return openFileOnClickedText(component, file, CommonComponents.GUI_OPEN_IN_BROWSER);
    }
    public static MutableComponent openFileOnClickedText(Component component, String file, Component hoverText) {
        return openFileOnClickedText(component, file, hoverText, ChatFormatting.GREEN);
    }
    public static MutableComponent openFileOnClickedText(Component component, String file, Component hoverText, ChatFormatting textColor) {
        return eventOnClickedText(component, ClickEvent.Action.OPEN_FILE, file, HoverEvent.Action.SHOW_TEXT, hoverText, textColor);
    }

    public static MutableComponent openUrlOnClickedText(Component component) {
        return openUrlOnClickedText(component, component.getString());
    }
    public static MutableComponent openUrlOnClickedText(Component component, String url) {
        return openUrlOnClickedText(component, url, CommonComponents.GUI_OPEN_IN_BROWSER);
    }
    public static MutableComponent openUrlOnClickedText(Component component, String url, Component hoverText) {
        return openUrlOnClickedText(component, url, hoverText, ChatFormatting.GREEN);
    }
    public static MutableComponent openUrlOnClickedText(Component component, String url, Component hoverText, ChatFormatting textColor) {
        return eventOnClickedText(component, ClickEvent.Action.OPEN_URL, url, HoverEvent.Action.SHOW_TEXT, hoverText, textColor);
    }

    public static MutableComponent showItemInfoText(Component component, ItemStack stack) {
        return showItemInfoText(component, ChatFormatting.BLUE, stack);
    }
    public static MutableComponent showItemInfoText(Component component, ChatFormatting textColor, ItemStack stack) {
        return ComponentUtils.wrapInSquareBrackets(((MutableComponent)component).withStyle((style) ->
                style.withColor(textColor).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(stack)))
        ));
    }

    public static MutableComponent showEntityInfoText(Component component, HoverEvent.EntityTooltipInfo entityInfo) {
        return showEntityInfoText(component, ChatFormatting.BLUE, entityInfo);
    }
    public static MutableComponent showEntityInfoText(Component component, ChatFormatting textColor, HoverEvent.EntityTooltipInfo entityInfo) {
        return ComponentUtils.wrapInSquareBrackets(((MutableComponent)component).withStyle((style) ->
                style.withColor(textColor).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, entityInfo))
        ));
    }

    public static <T> MutableComponent eventOnClickedText(Component component, ClickEvent.Action clickAction, String clickValue, HoverEvent.Action<T> hoverAction, T hoverValue, ChatFormatting textColor) {
        return ComponentUtils.wrapInSquareBrackets(((MutableComponent)component).withStyle((style) ->
            style.withColor(textColor)
                    .withClickEvent(new ClickEvent(clickAction, clickValue))
                    .withHoverEvent(new HoverEvent(hoverAction, hoverValue))
                    .withInsertion(clickValue)
        ));
    }

    public static List<FormattedCharSequence> split(String text, int maxLength, boolean useMcWarp) {
        return split(Component.literal(text), maxLength, useMcWarp);
    }

    public static List<FormattedCharSequence> split(Component text, int maxLength, boolean useMcWarp) {
        if (useMcWarp) {
            return RenderUtils.getFontRenderer().split(text, maxLength);
        }else {
            List<FormattedCharSequence> list = new ArrayList<>();
            if (RenderUtils.width(text) <= maxLength) {
                list.add(FormattedCharSequence.forward(text.getString(), Style.EMPTY));
            }else {
                StringBuilder sb = new StringBuilder();
                for (char c : text.getString().toCharArray()) {
                    if (c == '\n') {
                        list.add(FormattedCharSequence.forward(sb.toString(), Style.EMPTY));
                        sb.setLength(0);
                    } else {
                        sb.append(c);
                        if (RenderUtils.width(sb.toString()) >= maxLength) {
                            list.add(FormattedCharSequence.forward(sb.toString(), Style.EMPTY));
                            sb.setLength(0);
                        }
                    }
                }
                if (!sb.isEmpty()) {
                    list.add(FormattedCharSequence.forward(sb.toString(), Style.EMPTY));
                }
            }
            return list;
        }
    }
}
