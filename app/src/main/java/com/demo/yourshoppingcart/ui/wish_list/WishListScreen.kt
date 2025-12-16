package com.demo.yourshoppingcart.ui.wish_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.demo.yourshoppingcart.common.EmptyView
import com.demo.yourshoppingcart.common.ErrorView
import com.demo.yourshoppingcart.common.LoadingView
import com.demo.yourshoppingcart.ui.wish_list.component.EmptyWishListView
import com.demo.yourshoppingcart.ui.wish_list.component.WishListItemCard

@Composable
fun WishListScreen(
    onProductClick: (String) -> Unit,
    viewModel: WishListViewModel
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is WishListState.Loading -> {
            LoadingView()
        }

        is WishListState.Error -> {
            ErrorView((state as WishListState.Error).message)
        }

        is WishListState.Success -> {
            val list = (state as WishListState.Success).list

            if (list.isEmpty()) {
                EmptyView(
                    icon = Icons.Default.Favorite,
                    title = "Your wishlist is empty",
                    subTitle = "Save items you like ❤️"
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(list, key = { it.id }) { item ->
                        WishListItemCard(
                            wishList = item,
                            onRemove = { viewModel.removeWishList(item.id) },
                            onClick = {
                                item.product?.productId?.let(onProductClick)
                            }
                        )
                    }
                }
            }
        }
    }
}
