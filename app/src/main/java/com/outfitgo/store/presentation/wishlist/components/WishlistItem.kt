package com.outfitgo.store.presentation.wishlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.outfitgo.store.R
import com.outfitgo.store.core.util.CurrencyExchange
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme

@Composable
fun WishlistItem(
    product: Product,
    onRemoveClicked: (product: Product) -> Unit,
    onClicked: (product: Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier, onClick = {
        onClicked(product)
    }, elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imageUrl, contentDescription = product.name,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillBounds
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                // name
                Text(product.name, style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(8.dp))
                // vendor
                Text(product.vendor, modifier = Modifier.alpha(0.8f))

                Spacer(Modifier.height(8.dp))
                // price
                Text(
                    "${product.price} ${CurrencyExchange.currentCurrencyUnit}",
                    fontWeight = FontWeight.Bold
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = { onRemoveClicked(product) }) {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.delete_item_from_wishlist),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WishlistItemPreview() {
    OutfitGoTheme {
        LazyColumn {
            items(count = 3) {
                WishlistItem(
                    product = Product(
                        name = "Product number $it Product number $it Product number $it",
                        type = "T-Shirt",
                        vendor = "Nike",
                        price = "22.45",
                        imageUrl = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBw8SEBUPEBAVDxIQEA8PEBAPDw8PEBANFhUWFxURFRUYHSggGBolGxYVITEhJSkrLi4uFx8zODMsNygtLisBCgoKDQ0NFw8PFS0ZFRkrKy03Ky0rNysrLSstLSsrNy0rNy0tKysrKy03LSstKysrKystKys3KysrKysrKysrK//AABEIARMAtwMBIgACEQEDEQH/xAAcAAEAAAcBAAAAAAAAAAAAAAAAAQIDBAUHCAb/xABQEAACAQMABQgCDQUNCQAAAAAAAQIDBBEFBxIhMQYTQVFhcYGRIrEUJDI0QlJykqGissHRJWRzdMIVFiMzQ0VTY5Sjw+HwFzVEVGKCk9Px/8QAFQEBAQAAAAAAAAAAAAAAAAAAAAH/xAAWEQEBAQAAAAAAAAAAAAAAAAAAEQH/2gAMAwEAAhEDEQA/AN4gAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWek9KW9vDnLitChBvClUmoJy6lniwLwHno8uNEv8AnC38a0I+sqrljop/zhbf2mj+IGcBg/34aLzj90Lb+00vxM1CaklKLUk0mmmmmnwaYEwAAAAAAAAAAAAAAAAAAAAAAYbTfKmxtE+fuIxkv5OL26vzI713vCAzJoTX9pnnLqnaweVbU9qeH/K1MNr5qh5nqdOa5LaNKStaFSVVpqEqypxpp43T9GTcsPG7d3o0ZpS+qV6sq1STnOpJznKXGUnxYGM519ZHnn1ktSKT7Hwx0Eqx/rIFaNZ54nSWpLTCraP5hyzO2qOGM7+al6UX3Zc14HNdJLPdv8T03I/lPXsK6r0Xv4SjLfCpDphJdT+jCKOsQa30HrctakV7KpSt5N+6g41KeOh8VL6Ge20Vp+zuVm3uIVG9+ypYn4we9eRBkgAAAAAAAAAAAAAA8frE5ZrR9KMYJSr1s7CfCEF8Nrv3Lx6gPS6R0nQt47derGlHfjbkk33Li/A8Bp7W1Qp5jbUudf8ASVHsx71Fb2u9o1BpjT1e5m6lWpKpJ9Mnnd1di7DFOowPYac1haRuMqVaUIP4FGXNRx1ejva72zy07xvjv9ZbbaINoCaos8HufFFjVpNPs6y6ISKMdXhldq3+Baoy7guleK3HrKfJ3RX7ju6aqeyeanNT23hV1W5qFPZ4bLlhcM4y8romjw1GO7te/wAC6t6Gd73L1k6proXjxKiLRdxnFb28+bK9HSEovMZNNb1jdgxyJlnuINiaB1o31HEZy9kRW7FZNvHZL3We9s9/oTWnZ1cRrQlQk+nPOQ81vXkc/KSXaTwrS6NwHWtne0q0dulUjUi+mDT8+ornL+gOVNxazU6VRxa4roa6mulHQPIrlLC/tlWjunF7FWHVP4y7Gt/mugDPgAAAAAAAhKSSy9yW9t8EjmLl5p13l7VrZzDacKS6qMd0fx72ze+sjSfsfRtaSeJVEqEe+bw/q7RzRLe2BTUiOSR8SKYEWQIkMgCGRkZAHra2lqX7iRsko4bhUjPaq847vn1UqU3Dm8JKMpra2sPZXWeSyZm4f5Morruqj78Ke8gwyI4IBFESOSAAEWyBACJ73VJyhdtfQpSlincYozT4bT9xLwlhd0meBKlCq4yUk8NNNNcU1wYHYAMfyev/AGRaUa/TUo05S+Xj0l55MgAAAAAAau18Xmzb29HPu6tSp8yGz/iGkpG0dfdzm6oUviUHPxnNr9g1XLiBLU4hkrf0cSaKAECIAgQJiAEDLXMvydQXVc1m/r48TFGQr+8qPZXqru4v7wMdgiCIE/NMkLhNFCXF97AlZHBBEwFOT3kM70u0jUJKLzPuQHSuqO75zRkY5zzVSpT88TX2z2hq7UVdZo3NL4k6FT50HH9g2iAAAAAAc9a6Lnb0pOP9FTo0/qKf7Z4Cq8YZ6fWHc85pK6lx9sVILug9hfZPMXC9HuAp1PdJ9e5lWJaOpvj34LqIE2CBEAQBEgBAyNZe0aT/ADmsu70VuMeZGv7wo/rNf1RAxpEABgEQBBEQiAFGsyWy35faQuJbiaxXo94G4dQ9x7ZuafxqMJ/Mlj9s3SaA1I18aT2f6ShWj5bMv2Tf4AAACE5JJt8Em33ETG8pbjm7K4qcHC2ryXyth4+nAHLmlK7qValR8ZznN98m395ZSWYtdjK1Xi+9lJAYtveu9GQMdXWGZDIFTIJckUBEgRAEEZG4ftGiv6+u8eCRjy9rv2nR/TVQLHBHAAAAAGS9AzkMCzu3uLi1WIIs7t7zIUV6KA9nqmq7OlbftdSPzqckdInL/IKtsaStpfnFLyckmdQAAAAPL6zLjY0XcPplGnT+dUin9GT1B4TXNW2dG4+PXpR8EpS+5Ac+yKeCpgkAxd0uPcXlN5SfYi0vOkuLZ+hHuQEa08LPbH1orxZQuF6D7s/eVqT3LuAnIZIgCGS9re9KX6aqWWC/q+8qT/OKv2UBYAEQIEs3uJ0SVnhMChRfT2sqtlO3jiK8/PeTNgWNTfLxMpDgYukszRlEwMloCtsXNKfxakJeTydYHI1i/wCEj3o6w0ZV26FKfx6VOfnFMC5AAA1zrwnixprruE/KE/xNjGtdej9p0f1j9iT+4DRpIyckqcCjG3iJ7V+il4ELwlsX9AFxcv0H2pLz3FWksLBRrfBXbnwS/wDhcx4EAjgACVl9V940f1it4bkWTL6t7yo/rFb1R3gWDIogRAIlrRymToMC3pP0V2LHluJazwn3MjTW9x7d3j/plK6fovw9aAo2i9IySMfZLe33F/ECvbv013o6o5LSzY2z/NqH2InKtL3R1LyMlnR9q/zal9lAZkAADWOvaXtaguutN+Uf8zZxq3Xx/EW36Sr9mIGlSWS3E+NxTYFjdLcUbLp7y4uEW1o977wL6q05xwsYhiW5LMs8d3l24yVkUacd5XAgCIAlaL6t7yo/p63qX4Fky/uF7SoP+ur+tgY/AAAIiyBECnQrOnU20s5i0s5XFSTax04kWl37h+HrRc1o5ZZ3b9HHagJrFbvIvYFpZrcXVMCrB7zqDkDLOjLV/wBRFeWUcvLidM6s550VbP8A6JryqTQHpwAANU6+p/wdrHrlcPyVNfebWNSa+3utV+sf4YGn8EjZOSMotbheotbSO/z9bLyqWls9+O1kF7BFVIpxW8qgQBEgBKZS6XtCh21q7797WfoMa0ZK7942/wClr/blwAxTJgSoCJFhhgUqnWWN4+C7S+fAsLh+ku8C5oLCLimijBFeIEUt50jqmnnRNDsdZf3kn95zdE6N1QS/JVLsnWX1mB7QAADUOvvja/JuPXTNvGoNfa9K1+TX9dMDUTZIydkrAoVDNX/JKvSsbfSXGlcqopddKrGpNRTWPcypwUk+vaXUYaZuHlZPY5K2Ufjws/ppyn9wGoKTXQ0+7DKhjM7ydSfW/MsGQIZLDafW/MvKfBdxBOZSpFuwhLohcThv65Pa3GKMopfk/H51nwcenyAxbIMmKdSO5gTjO7PeWO0yVyYFeVSOOPkemqclI0tDLSlVZqXN1To26y1zdBKptza4NycMb+CXaePNu6XlznJK3b/krvH16yX0TA1bTK0FuKUEVlwAikdC6l5Z0WuyvVX2TnuBv3Ug/wAmzXVdVF/d0gNhAAAak18037Wl0Yrxz25ps22eL1taJ5/R05pZnbyVZY47HCfhh5/7QOeCVtEak0unwKUascf5FEGjaXL6q1yc0ZHDxKFu89GVQeF45fkzWVnSdWcadNOU5yjCEcb5Tk0kl3tnU1Hk/QlY07C4pwr0qdCjRlGccxk6cUlJdTysp8UQciEyOgdJ6kdGTbdCrXts/BU41oL/AMicvrGFrah3n0NJYXQp2eX5qqvUWjTJe0X6K7jZdfUVeL3F7Rn8ulVp+pyLC41R6Xp7owpV0uDpV4rPhU2QPDGQUH7C2ujn0t3XjpMxPV5phPDsanhOhL7M2ZmGrnSzteZ9ipPPP761BNz3pQ91xxj8SDXpLU4PuPYU9Wemn/wTj8qvaf8AsLqlqm0xJ4dKnTz8Kdensrv2Mv6ANcYIYNs22ou8f8Ze0IdkKVWr9LcTK2Woikv4/SE5rqoW8KT85yn6gNHm1s1P3pPag4xd7DmpP4VLbj6a7NpSXgbD0Pqm0NbtSdu7qS+FdzdVPvprEH80r62LBT0NcQisc1GlViopJKNKpGTSXR6KYHNcfuRWXApycU+P0Eecj1gTrczoHUnHGjG8e6uarXb6NNfcc90PSkkul8Mb2dTch9Eu10fQoSWJqmp1E+Kqze1JPubx4AZ0AACWpTUk4ySlGScZJrKcXuaaJgByly0sI0L+4owWI07irGC6obT2V5YMGep1kf7zuv1ip6zyzKNhakNHqrpNTks8xRq1ln4+6Cf18+B0OaG1BVcX9WPxrSeO9VKZvkaAAIAAAAAAAAAAAElejGcZQnFSjOLhKMlmMoNYcWulNE4A5Q5b6JhaX9e2p52KVTZhtPLVNpSim+nc1vMCe21vwxpe57XSfnRpnikB7zUzounX0nDnI7SownXSe9OpHCi33OSfekdIGhdQcPb9R9VpUf8AeUl95voAAAAAA1npbVHTurmtc1ryceeq1KihSpQWypSbS2pN58kWz1HWXReXHiqD9UEbVAHgeRerWOjrr2TC6daPNzp7EqKjL0sb9pS7Oo98AAAAAAAAAAAAAAAAABrblVqqV/fVbupdulGpzeKcKKlJbMIxeZOWPg9RYf7DLP8A524z8mhjy2TbAA1/yD1cy0bdzuFdKvCdGdJRdF05rMoS3vaafuew2AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH/2Q==",
                        id = "", pageCursor = ""
                    ),
                    onRemoveClicked = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClicked = {}
                )
            }
        }
    }
}