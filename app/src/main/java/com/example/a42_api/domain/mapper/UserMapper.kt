package com.example.a42_api.domain.mapper

import com.example.a42_api.BaseMapper
import com.example.a42_api.domain.models.*

class UserMapper: BaseMapper() {

    fun mapToken(token: Token): Token =
        Token(
            token.accessToken,
            mapTokenType(token.tokenType),
            token.expiresIn
        )

    private fun mapTokenType(tokenType: String): String =
        firstUpperCase(tokenType) + " "

    fun mapCoalitions(
        coalitionIds: List<CoalitionId>,
        coalitions: List<Coalition>
    ): List<Coalition> =
        coalitionIds.map { coalition ->
            coalitions.find {
                it.id == coalition.id
            } ?: Coalition().let {
                Coalition(
                    it.id,
                    firstUpperCase(it.name),
                    it.imageUrl,
                    it.backgroundUrl,
                    it.color
                )
            }
        }
}