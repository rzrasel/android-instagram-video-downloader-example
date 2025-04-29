package com.rzrasel.instagramvideodownload.data.remote.dto

import com.google.gson.annotations.SerializedName

data class IGGraphQLResponseDto(
    @SerializedName("data") val data: IGShortcodeMediaData?,
    @SerializedName("status") val status: String?,
    @SerializedName("message") val message: String?
)

data class IGShortcodeMediaData(
    @SerializedName("shortcode_media") val shortcodeMedia: IGShortcodeMedia?,
    @SerializedName("xdt_shortcode_media") val xdtShortcodeMedia: IGShortcodeMedia?
)

data class IGShortcodeMedia(
    @SerializedName("is_video") val isVideo: Boolean?,
    @SerializedName("video_url") val videoUrl: String?
)