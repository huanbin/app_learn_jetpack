package com.hb.lintchecklib

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue

class DarkThemeLintRegistry : IssueRegistry() {

    override val issues: List<Issue>
        get() = listOf(DirectColorUseIssue.ISSUE)
}