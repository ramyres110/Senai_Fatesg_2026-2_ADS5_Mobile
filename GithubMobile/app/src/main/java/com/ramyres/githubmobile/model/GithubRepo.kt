package com.ramyres.githubmobile.model

data class GithubRepo(
    val id: ULong,
    val node_id: String,
    val name: String,
    val full_name: String,
    val watchers_count: UInt,
    val stargazers_count: UInt,
    val private: Boolean,
    val created_at: String,
    val updated_at: String,
    val git_url: String,
    val visibility: String,
    val forks: UInt,
    val open_issues: UInt,
    val default_branch: String
)