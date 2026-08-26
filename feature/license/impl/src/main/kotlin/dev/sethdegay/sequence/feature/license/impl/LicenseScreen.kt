package dev.sethdegay.sequence.feature.license.impl

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.LicenseDialogBody
import com.mikepenz.aboutlibraries.ui.compose.m3.libraryColors
import com.mikepenz.aboutlibraries.ui.compose.m3.style.m3VariantTextStyles
import com.mikepenz.aboutlibraries.ui.compose.variant.LibrariesVariant
import com.mikepenz.aboutlibraries.ui.compose.variant.LibraryBadges
import com.mikepenz.aboutlibraries.ui.compose.variant.LibraryDetailMode
import dev.sethdegay.sequence.core.designsystem.R.string.navigate_up_content_description
import dev.sethdegay.sequence.core.designsystem.icon.SequenceIcons
import dev.sethdegay.sequence.core.designsystem.util.IconButton
import dev.sethdegay.sequence.feature.license.impl.R.string

@Composable
fun LicenseScreen(navigateUp: () -> Unit) {
    val licenses by produceLibraries(R.raw.licenses)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(string.license_top_app_bar_title))
                },
                navigationIcon = {
                    SequenceIcons.NavigateUp.IconButton(
                        onClick = dropUnlessResumed { navigateUp() },
                        contentDescription = stringResource(navigate_up_content_description),
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { scaffoldPadding ->
        LibrariesContainer(
            modifier = Modifier
                .consumeWindowInsets(scaffoldPadding)
                .fillMaxSize(),
            contentPadding = scaffoldPadding,
            libraries = licenses,
            badges = LibraryBadges(
                version = false,
                author = false,
                description = false,
                license = false,
                funding = false,
            ),
            licenseDialogBody = { library, modifier ->
                LicenseDialogBody(
                    library = library,
                    colors = LibraryDefaults.libraryColors(),
                    modifier = modifier.padding(16.dp),
                )
            },
            licenseDialogConfirmText = stringResource(string.license_dialog_confirm_text),
            detailMode = LibraryDetailMode.Dialog,
            variant = LibrariesVariant.Traditional,
            variantTextStyles = LibraryDefaults.m3VariantTextStyles(
                nameTextStyle = MaterialTheme.typography.bodyLarge,
            ),
        )
    }
}