package com.moviles.clothingapp.view.Discover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.moviles.clothingapp.model.PostData
import com.moviles.clothingapp.ui.theme.figtreeFamily


/* PostItem component:
*   - Responsable of the look and data display of a post.
*   - It displays them in cards with: brand, name and price
*   - Uses the same format as the featured products section for each post.
 */
@Composable
fun PostItem(post: PostData) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = post.image,
                contentDescription = post.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.brand,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontFamily = figtreeFamily,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = post.name,
                fontFamily = figtreeFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = "$${post.price}",
                fontFamily = figtreeFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}