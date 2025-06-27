package com.example.android.architecture.blueprints.todoapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

sealed class TreeNode(val name: String) {
    class Directory(name: String, val children: List<TreeNode> = emptyList()) : TreeNode(name) {
        var isExpanded = false
    }
    class File(name: String) : TreeNode(name)
}

@Composable
fun TreeItem(node: TreeNode, level: Int = 0) {
    val padding = (level * 16).dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (node is TreeNode.Directory) {
                    node.isExpanded = !node.isExpanded
                }
            }
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Box(modifier = Modifier.width(padding))
        when (node) {
            is TreeNode.Directory -> {
                Icon(
                    imageVector = if (node.isExpanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowRight,
                    contentDescription = null
                )
                Text(
                    text = node.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 4.dp)
                )
                if (node.isExpanded) {
                    LazyColumn(modifier = Modifier.padding(start = 16.dp)) {
                        items(node.children.size) { index ->
                            TreeItem(node = node.children[index], level = level + 1)
                        }
                    }
                }
            }
            is TreeNode.File -> {
                Icon(
                    imageVector = Icons.Default.InsertDriveFile,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = node.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun FileTree() {
    val sampleTree = rememberSaveable {
        TreeNode.Directory("app", listOf(
            TreeNode.Directory("src", listOf(
                TreeNode.Directory("main", listOf(
                    TreeNode.File("AndroidManifest.xml"),
                    TreeNode.Directory("java", listOf(
                        TreeNode.Directory("com", listOf(
                            TreeNode.Directory("example", listOf(
                                TreeNode.File("ActionManager.kt")
                            ))
                        ))
                    ))
                )),
                TreeNode.File("build.gradle")
            ))
        ))
    }
    
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            TreeItem(node = sampleTree)
        }
    }
}
