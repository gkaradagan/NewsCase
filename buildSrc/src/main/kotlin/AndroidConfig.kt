object AndroidConfig {
    const val COMPILE_SDK_VERSION = 29
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 29
    const val BUILD_TOOLS_VERSION = "29.0.2"

    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0"

    const val ID = "com.gorkem.news"
    const val TEST_INSTRUMENTATION_RUNNER = "android.support.test.runner.AndroidJUnitRunner"
    const val SUPPORT_LIBRARY_VECTOR_DRAWABLES = true
    const val API_KEY = "ae68088e70d04639b4950bdc9d546924"
}

interface BuildType {

    companion object {
        const val RELEASE = "release"
        const val DEBUG = "release"
    }

    val isMinifyEnabled: Boolean
}

object BuildTypeDebug : BuildType {
    override val isMinifyEnabled = false

}

object BuildTypeRelease : BuildType {
    override val isMinifyEnabled = true
}
