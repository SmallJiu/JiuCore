package cat.jiu.core;

import cat.jiu.core.util.registry.DynamicLanguageProvider;

class Languages {
    static void bootstrap(DynamicLanguageProvider provider) {
        en_us(provider);
        zh_cn(provider);
    }

    private static void en_us(DynamicLanguageProvider provider) {
        provider.setLanguageCode("en_us");
        provider.add("info.hold.un", "§7Hold §f%s §7to show more message.");
        provider.add("info.config.clear", "Clear");
        provider.add("info.config.reload", "Reload");
        provider.add("info.config.undo", "Undo Changes");
        provider.add("info.config.reset", "Reset to Default");
        provider.add("info.config.save", "Save and Exit");
        provider.add("info.config.save.0", "Use 'Esc' Exit but not Save");
        provider.add("info.config.save.1", "Save");
        provider.add("info.config.world_restart", "Need restart world.");
        provider.add("info.config.not_enough_permissions", "You do not have enough permissions to edit the server config. You can still look at the current values here though.");
    }
    private static void zh_cn(DynamicLanguageProvider provider) {
        provider.setLanguageCode("zh_cn");
        provider.add("info.hold.un", "§7按住 §f%s §7以显示更多信息.");
        provider.add("info.config.clear", "清空");
        provider.add("info.config.reload", "重载");
        provider.add("info.config.undo", "撤回操作");
        provider.add("info.config.reset", "重置至默认");
        provider.add("info.config.save", "保存并退出");
        provider.add("info.config.save.0", "使用 'Esc键' 退出但不保存配置文件");
        provider.add("info.config.save.1", "保存");
        provider.add("info.config.world_restart", "需要重启世界");
        provider.add("info.config.not_enough_permissions", "你没有足够的权限来编辑此服务器配置，但你仍可查看。");
    }
}
