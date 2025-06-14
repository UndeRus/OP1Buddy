package org.jugregator.op1buddy.features.sync.ui.screens

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.ui.icons.Icons
import org.jugregator.op1buddy.ui.icons.faq.Page1
import org.jugregator.op1buddy.ui.icons.faq.Page2
import org.jugregator.op1buddy.ui.icons.faq.Page3
import org.jugregator.op1buddy.ui.icons.faq.Page4
import org.jugregator.op1buddy.ui.icons.faq.Page5
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun DeviceNotConnectedScreen(modifier: Modifier = Modifier) {
    val items =
        listOf(
            FaqItem(0, Icons.Faq.Page1, R.string.faq1_description),
            FaqItem(1, Icons.Faq.Page2, R.string.faq2_description),
            FaqItem(2, Icons.Faq.Page3,  R.string.faq3_description),
            FaqItem(3, Icons.Faq.Page4, R.string.faq4_description),
            FaqItem(4, Icons.Faq.Page5, R.string.faq5_description),
        )
    val pagerState = rememberPagerState(pageCount = { items.size })
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            HorizontalPager(
                state = pagerState,
                pageContent = { index ->
                    FaqItemView(
                        modifier = Modifier.fillMaxWidth(),
                        item = items[index]
                    )
                })

            Spacer(Modifier.height(32.dp))
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FaqItemView(modifier: Modifier = Modifier, item: FaqItem) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            imageVector = item.image,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.widthIn(max = 286.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = stringResource(item.contentDescriptionResId),
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary),
            textAlign = TextAlign.Center
        )
    }
}

data class FaqItem(
    val id: Int,
    val image: ImageVector,
    @StringRes val contentDescriptionResId: Int
)

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
@Composable
fun DeviceNotConnectedScreenPreview() {
    AppTheme {
        DeviceNotConnectedScreen()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
@Composable
fun FaqItemViewPreview() {
    AppTheme {
        FaqItemView(modifier = Modifier.fillMaxSize(), item = FaqItem(0, Icons.Faq.Page1, R.string.project_id))
    }
}
