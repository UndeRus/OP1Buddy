package org.jugregator.op1buddy.features.sync.ui.screens

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun DeviceNotConnectedScreen(modifier: Modifier = Modifier) {
    val items =
        listOf(
            FaqItem(0, R.drawable.faq_page1, R.string.faq1_description),
            FaqItem(0, R.drawable.faq_page2, R.string.faq2_description),
            FaqItem(0, R.drawable.faq_page3, R.string.faq3_description),
            FaqItem(0, R.drawable.faq_page4, R.string.faq4_description),
            FaqItem(0, R.drawable.faq_page5, R.string.faq5_description),
        )
    val pagerState = rememberPagerState(pageCount = { items.size})
    Box (modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            HorizontalPager(
                modifier = Modifier.fillMaxWidth(),
                state = pagerState,
                //contentPadding = PaddingValues(16.dp),
                pageContent = { index ->
                    FaqItemView(modifier = Modifier.fillMaxWidth(), item = items[index])
                })

            Spacer(Modifier.height(8.dp))
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
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = item.imageResId),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(item.contentDescriptionResId),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

data class FaqItem(
    val id: Int,
    @DrawableRes val imageResId: Int,
    @StringRes val contentDescriptionResId: Int
)

@Preview
@Composable
fun DeviceNotConnectedScreenPreview() {
    AppTheme {
        DeviceNotConnectedScreen()
    }
}

@Preview
@Composable
fun FaqItemViewPreview() {
    AppTheme {
        FaqItemView(item = FaqItem(0, R.drawable.faq_page1, R.string.project_id))
    }
}
