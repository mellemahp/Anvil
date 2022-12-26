load("//:constants.bzl", "JAVAC_OPTS")

java_library(
    name = "anvil-core",
    srcs = glob(["src/main/**/*.java"]),
    plugins = [
        ":nullaway_plugin",
    ],
    deps = [
        # pulls in nullable annotation
        "@maven//:com_google_code_findbugs_jsr305",
        # vertx dependencies
        "@maven//:io_vertx_vertx_core",
        "@maven//:io_vertx_vertx_web_openapi",
        "@maven//:io_vertx_vertx_web_validation",
        "@maven//:io_vertx_vertx_web",
        "@maven//:io_vertx_vertx_json_schema", # MAYBE REMOVE?
        "@maven//:io_vertx_vertx_health_check",
        # bean validation
        "@maven//:org_hibernate_validator_hibernate_validator",
        "@maven//:org_glassfish_jakarta_el",
        "@maven//:jakarta_validation_jakarta_validation_api",
    ],
    javacopts = JAVAC_OPTS,
    visibility = ["//visibility:public"],
)