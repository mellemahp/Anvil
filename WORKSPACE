workspace(name = "commentator")

#######################
# Base bazel tool imports
#######################
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

#######################
# Java Library Imports
#######################
# import libraries from maven
RULES_JVM_EXTERNAL_TAG = "4.2"

RULES_JVM_EXTERNAL_SHA = "cd1a77b7b02e8e008439ca76fd34f5b07aecb8c752961f9640dea15e9e5ba1ca"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")

rules_jvm_external_deps()

load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")

rules_jvm_external_setup()

load("@rules_jvm_external//:defs.bzl", "maven_install")

#####################
# Java Dependencies #
#####################
load("//:dependencies.bzl", "JAVA_DEPENDENCIES", "MAVEN_REPOS")
maven_install(
    name = "maven",
    artifacts = JAVA_DEPENDENCIES,
    repositories = MAVEN_REPOS,
)

#############
# Test Deps #
#############
load("//:dependencies.bzl", "JAVA_TEST_DEPENDENCIES")
maven_install(
    name = "maven_test",
    artifacts = JAVA_TEST_DEPENDENCIES,
    repositories = MAVEN_REPOS,
)


###################
# SGF4J package
###################
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

git_repository(
    name = "sgf4j",
    branch = "main",
    remote = "https://github.com/mellemahp/sgf4j",
)

####################
# Protobuff support
####################
http_archive(
    name = "rules_proto_grpc",
    sha256 = "fb7fc7a3c19a92b2f15ed7c4ffb2983e956625c1436f57a3430b897ba9864059",
    strip_prefix = "rules_proto_grpc-4.3.0",
    urls = ["https://github.com/rules-proto-grpc/rules_proto_grpc/archive/4.3.0.tar.gz"],
)

load("@rules_proto_grpc//:repositories.bzl", "rules_proto_grpc_repos", "rules_proto_grpc_toolchains")
rules_proto_grpc_toolchains()
rules_proto_grpc_repos()

load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")
rules_proto_dependencies()
rules_proto_toolchains()

load("@rules_proto_grpc//java:repositories.bzl", rules_proto_grpc_java_repos = "java_repos")
rules_proto_grpc_java_repos()