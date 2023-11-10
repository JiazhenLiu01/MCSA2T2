package io.github.sceneview.sample.arcursorplacement

interface LocationCallback {
    fun onLocationResult(latitude: Double, longitude: Double, altitude: Double,address:String)
    fun onPermissionDenied()
    fun onLocationUnavailable()
}