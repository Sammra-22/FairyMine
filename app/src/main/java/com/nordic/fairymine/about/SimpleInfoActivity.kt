package com.nordic.fairymine.about

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.method.LinkMovementMethod
import com.nordic.fairymine.R
import com.nordic.fairymine.common.utils.color
import com.nordic.fairymine.common.utils.formatHighlightedText
import com.nordic.fairymine.common.utils.parseHtml
import com.nordic.fairymine.common.view.BaseActivity
import com.nordic.fairymine.databinding.ActivityAboutBinding

/**
 * Created by Sam22 on 3/20/18.
 */
class SimpleInfoActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityAboutBinding>(this, R.layout.activity_about)
        binding.body.movementMethod = LinkMovementMethod.getInstance()

        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener { finish() }
        }
        val title = intent.getStringExtra(EXTRA_TITLE)
        val body = intent.getStringExtra(EXTRA_BODY)
        val highlights = intent.getStringArrayExtra(EXTRA_HIGHLIGHTS)
        val urls = intent.getStringArrayExtra(EXTRA_LINKS)

        if (title != null && body != null) {
            val bodyText = if (highlights != null) {
                formatHighlightedContent(body, highlights, urls)
            } else {
                body.parseHtml()
            }
            binding.viewModel = SimpleInfoVM(title, bodyText)
        } else {
            finish()
        }
    }

    private fun formatHighlightedContent(content: String, highlights: Array<String>, urls: Array<String>): Spannable {
        return content.formatHighlightedText(highlights, urls, color(R.color.links), { link ->
            if (link.contains("@")) {
                Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(link))
                    if (resolveActivity(packageManager) != null) {
                        startActivity(this)
                    }
                }
                true
            } else {
                false
            }
        })
    }

    companion object {

        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_BODY = "EXTRA_BODY"
        private const val EXTRA_HIGHLIGHTS = "EXTRA_HIGHLIGHTS"
        private const val EXTRA_LINKS = "EXTRA_LINKS"

        fun newIntent(context: Context, title: String, body: String): Intent =
                Intent(context, SimpleInfoActivity::class.java)
                        .putExtra(EXTRA_TITLE, title)
                        .putExtra(EXTRA_BODY, body)

        fun newIntent(context: Context, title: String, body: String, highlights: Array<String>, links: Array<String>): Intent =
                newIntent(context, title, body)
                        .putExtra(EXTRA_HIGHLIGHTS, highlights)
                        .putExtra(EXTRA_LINKS, links)
    }
}