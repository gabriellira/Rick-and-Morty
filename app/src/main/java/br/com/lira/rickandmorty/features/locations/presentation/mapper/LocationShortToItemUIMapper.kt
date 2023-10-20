package br.com.lira.rickandmorty.features.locations.presentation.mapper

import br.com.lira.rickandmorty.features.locations.presentation.model.LocationItemUI
import br.com.lira.rickandmorty.features.shared.domain.model.LocationShort
import javax.inject.Inject

class LocationShortToItemUIMapper @Inject constructor() {

    fun mapFrom(location: LocationShort) = LocationItemUI(
        id = location.id,
        name = location.name,
        type = location.type
    )
}
