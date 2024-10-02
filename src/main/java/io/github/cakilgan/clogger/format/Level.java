package io.github.cakilgan.clogger.format;

public enum Level {
    DEBUG(CLColor.BLUE_BOLD_BRIGHT,CLColor.BLUE_BOLD,CLColor.BLUE,3),
    WARNING(CLColor.YELLOW_BOLD_BRIGHT,CLColor.YELLOW_BOLD,CLColor.YELLOW,2),
    ERROR(CLColor.RED_BOLD_BRIGHT,CLColor.RED_BOLD,CLColor.RED,4),
    FATAL(CLColor.RED_BOLD,CLColor.RED_UNDERLINED,CLColor.RED,5),
    INFO(CLColor.GREEN_BOLD_BRIGHT,CLColor.GREEN_BOLD,CLColor.GREEN,1),
    LIFECYCLE(CLColor.PURPLE_BOLD_BRIGHT,CLColor.PURPLE_BOLD,CLColor.PURPLE,0)
    ;

    public String message_color,level_color,bracket_color;
    int order;

    Level(String message_color, String level_color, String bracket_color,int order) {
        this.message_color = message_color;
        this.level_color = level_color;
        this.bracket_color = bracket_color;
        this.order = order;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
