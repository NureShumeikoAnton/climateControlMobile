package com.example.climatecontrolmobile.data.api

import com.example.climatecontrolmobile.data.model.Command
import com.example.climatecontrolmobile.data.model.Device
import com.example.climatecontrolmobile.data.model.FcmTokenRequest
import com.example.climatecontrolmobile.data.model.LoginRequest
import com.example.climatecontrolmobile.data.model.LoginResponse
import com.example.climatecontrolmobile.data.model.Measurement
import com.example.climatecontrolmobile.data.model.Profile
import com.example.climatecontrolmobile.data.model.Sensor
import com.example.climatecontrolmobile.data.model.System
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("systems")
    suspend fun getSystems(): List<System>

    @GET("systems/my-systems")
    suspend fun getUserSystems(): List<System>

    @GET("sensors")
    suspend fun getSensors(): List<Sensor>

    @GET("sensors/system/{systemId}")
    suspend fun getSensorsBySystemId(@Path("systemId") systemId: Int): List<Sensor>

    @GET("measurements/sensor/{sensorId}")
    suspend fun getMeasurements(@Path("sensorId") sensorId: Int): List<Measurement>

    @GET("devices")
    suspend fun getDevices(): List<Device>

    @GET("devices/system/{systemId}")
    suspend fun getDevicesBySystemId(@Path("systemId") systemId: Int): List<Device>

    @GET("commands/device/{deviceId}")
    suspend fun getCommands(@Path("deviceId") deviceId: Int): List<Command>

    @GET("profiles/system/{systemId}")
    suspend fun getProfilesBySystemId(@Path("systemId") systemId: Int): List<Profile>

    @PUT("systems/{systemId}/profile/{profileId}")
    suspend fun updateSystemProfile(
        @Path("systemId") systemId: Int,
        @Path("profileId") profileId: Int
    ): Response<System>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @PUT("users/fcm-token")
    suspend fun updateFcmToken(@Body fcmTokenRequest: FcmTokenRequest): Response<Unit>
}