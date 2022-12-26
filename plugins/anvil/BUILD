load("//:constants.bzl", "JAVAC_OPTS")

java_binary(
    name = "anvil-plugin-anvil",
    srcs = glob(["src/main/java/com/hmellema/anvil/plugin/**/*.java"]),
    plugins = [
        ":nullaway_plugin",
        "//plugins:extension_plugin"
    ],
    deps = [
        "@sgf4j//core:sgf4j-core",
        "@maven//:org_pf4j_pf4j",
        "@maven//:com_squareup_javapoet",
        "@maven//:software_amazon_smithy_smithy_model",
        "//anvil-core:anvil-core"
    ],
    javacopts = JAVAC_OPTS,
    deploy_manifest_lines = [
        "Plugin-Class: com.hmellema.anvil.plugins.AnvilPlugin",
        "Plugin-Id: anvil-plugin",
        "Plugin-Provider: Hunter Mellema",
        "Plugin-Version: 0.0.1"
    ],
    main_class = "does.not.exist",
    visibility = ["//visibility:public"],
)