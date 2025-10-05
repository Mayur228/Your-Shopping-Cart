package com.demo.yourshoppingcart.common.network

import com.demo.yourshoppingcart.common.network.model.AppRawResponse
import com.demo.yourshoppingcart.common.network.model.AppResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.util.AttributeKey
import io.ktor.utils.io.streams.asInput
import kotlinx.serialization.json.JsonElement
import org.koin.core.annotation.Single

@Single
class AppHttpClient(
//    val httpClient: HttpClient
) {
    companion object {
        val APP_REQUEST_URL = AttributeKey<AppApiRequest>("AppRequestUrl")
    }

    /*    suspend inline fun <reified T> request(
            request: AppApiRequest,
        ): T {

            return when (request) {
                is AppApiRequest.Get -> {
                    getResult<T>(request, request.queryParams)
                }

                is AppApiRequest.Post<*> -> {
                    postResult<T>(request, request.queryParams, request.body)
                }
            }
        }

        suspend fun requestIgnoreResponse(
            request: AppApiRequest,
        ) {
            return when (request) {
                is AppApiRequest.Get -> {
                    get(request, request.queryParams)
                }

                is AppApiRequest.Post<*> -> {
                    post(request, request.queryParams, request.body)
                }

            }
        }


        suspend fun get(url: AppApiRequest, queryParams: Map<String, String>? = null) {
            val httpResponse = httpClient.get(url.url) {
                setAttributes {
                    put(APP_REQUEST_URL, url)
                }
                url {
                    queryParams?.forEach { (key, value) ->
                        parameters.append(key, value)
                    }

                }
            }

            val response =
                httpResponse.body<AppRawResponse<JsonElement>>().asResponse(httpResponse.status.value)

            return when (response) {
                is AppResponse.Empty -> if (response.httpStatusCode == HttpStatusCode.OK.value) {
                    Unit
                } else {
                    throw AppApiException(response)
                }

                is AppResponse.Error -> throw AppApiException(response)
                is AppResponse.Data -> Unit
            }
        }

        suspend inline fun <reified T> getResult(
            url: AppApiRequest,
            queryParams: Map<String, String>? = null
        ): T {
            val httpResponse = httpClient.get(url.url) {
                setAttributes {
                    put(APP_REQUEST_URL, url)
                }
                url {
                    queryParams?.forEach { (key, value) ->
                        parameters.append(key, value)
                    }

                }
            }

            val response =
                httpResponse.body<AppRawResponse<T>>().asResponse(httpResponse.status.value)

            return when (response) {
                is AppResponse.Empty -> throw AppApiException(
                    response
                )

                is AppResponse.Error -> throw AppApiException(
                    response
                )

                is AppResponse.Data -> response.data
            }
        }

        suspend fun post(url: AppApiRequest, queryParams: Map<String, String>? = null, body: Any?) {
            val httpResponse = httpClient.post(url.url) {
                setAttributes {
                    put(APP_REQUEST_URL, url)
                }
                url {
                    queryParams?.forEach { (key, value) ->
                        parameters.append(key, value)
                    }

                }
                contentType(ContentType.Application.Json)
                if (body != null) {
                    setBody(body)
                }
            }

            val response =
                httpResponse.body<AppRawResponse<JsonElement>>().asResponse(httpResponse.status.value)

            return when (response) {
                is AppResponse.Empty -> if (response.httpStatusCode == HttpStatusCode.OK.value) {
                    Unit
                } else if (response.httpStatusCode == HttpStatusCode.Created.value) {
                    Unit
                } else {
                    throw AppApiException(response)
                }

                is AppResponse.Error -> throw AppApiException(response)
                is AppResponse.Data -> Unit
            }
        }

        suspend inline fun <reified T> postResult(
            url: AppApiRequest,
            queryParams: Map<String, String>? = null,
            body: Any?
        ): T {

            val httpResponse = httpClient.post(url.url) {
                setAttributes {
                    put(APP_REQUEST_URL, url)
                }
                url {
                    queryParams?.forEach { (key, value) ->
                        parameters.append(key, value)
                    }

                }
                contentType(ContentType.Application.Json)
                if (body != null) {
                    setBody(body)
                }
            }

            val response =
                httpResponse.body<AppRawResponse<T>>().asResponse(httpResponse.status.value)



            return when (response) {
                is AppResponse.Empty ->
                    throw throw AppApiException(response)

                is AppResponse.Error -> throw AppApiException(response)
                is AppResponse.Data -> response.data
            }
        }*/

}

