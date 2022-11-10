package com.awp.samakaki.ui.menu_silsilah_keluarga

import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.awp.samakaki.helper.SessionManager
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.utils.Edge
import com.awp.samakaki.viewmodel.FamilyTreeViewModel
import dev.bandb.graphview.decoration.edge.ArrowEdgeDecoration
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node
import dev.bandb.graphview.layouts.energy.FruchtermanReingoldLayoutManager
import dev.bandb.graphview.layouts.layered.SugiyamaArrowEdgeDecoration
import dev.bandb.graphview.layouts.layered.SugiyamaConfiguration
import dev.bandb.graphview.layouts.layered.SugiyamaLayoutManager
import dev.bandb.graphview.layouts.tree.BuchheimWalkerConfiguration
import dev.bandb.graphview.layouts.tree.BuchheimWalkerLayoutManager
import dev.bandb.graphview.layouts.tree.TreeEdgeDecoration

class SilsilahKeluargaTreeFragment : SilsilahKeluargaFragment() {

    private val familyTreeViewModel by viewModels<FamilyTreeViewModel>()

    override fun setLayoutManager() {
        val configuration = com.awp.samakaki.utils.BuchheimWalkerConfiguration.Builder()
            .setSiblingSeparation(100)
            .setLevelSeparation(100)
            .setSubtreeSeparation(100)
            .setOrientation(com.awp.samakaki.utils.BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
            .build()
        recyclerView.layoutManager = context?.let { com.awp.samakaki.utils.BuchheimWalkerLayoutManager(it, configuration) }
    }

//    override fun setLayoutManagerDataTop() {
//        val configuration = com.awp.samakaki.utils.BuchheimWalkerConfiguration.Builder()
//            .setSiblingSeparation(100)
//            .setLevelSeparation(100)
//            .setSubtreeSeparation(100)
//            .setOrientation(com.awp.samakaki.utils.BuchheimWalkerConfiguration.ORIENTATION_BOTTOM_TOP)
//            .build()
//        recyclerViewDataTop.layoutManager = context?.let { com.awp.samakaki.utils.BuchheimWalkerLayoutManager(it, configuration) }
//    }

    override fun setEdgeDecoration() {
        recyclerView.addItemDecoration(com.awp.samakaki.utils.TreeEdgeDecoration())
    }

//    override fun setEdgeDecorationDataTop() {
//        recyclerViewDataTop.addItemDecoration(com.awp.samakaki.utils.TreeEdgeDecoration())
//    }

    override fun createGraph(): com.awp.samakaki.utils.Graph {
        val graph = com.awp.samakaki.utils.Graph()
//        val node1 = com.awp.samakaki.utils.Node("parent")

        val token = context?.let { SessionManager.getToken(it) }
        familyTreeViewModel.findUserRelations("Bearer $token")
        familyTreeViewModel.findUserRelations.observe(viewLifecycleOwner) {
            when(it) {
                is BaseResponse.Success -> {
                    val node1 = com.awp.samakaki.utils.Node(it.data?.data?.currentUser?.name!!)
                    val dataRelation = it.data.data.relation.map { it.userRelated }.toSet().joinToString()
                    val resource = it.data.data.relation.map { it.relationName }.toSet().joinToString()


                    val exp = resource.contains("nenek_dari_ibu")
//                        listOf(resource).find { it.contains("nenek_dari_ibu") }
                    Log.d("TAG", "createGraphresource: $dataRelation ")
                    Log.d("TAG", "createGraphdataRelation: $resource ")
                    Log.d("TAG", "createGraph: $exp ")
                    val node13 = com.awp.samakaki.utils.Node(dataRelation)
                    when {
                        resource.contains("bapak") -> Log.e("TAG", "createGraph: punya bapak"  )
                    }



                    graph.addEdge(node1, node13)


                }

                is BaseResponse.Error -> textMessage(it.msg.toString())
            }
        }


        return graph
    }

//    override fun createGraphDataTop(): com.awp.samakaki.utils.Graph {
//        val graphDataTop = com.awp.samakaki.utils.Graph()
//        val node1 = com.awp.samakaki.utils.Node("parent")
//        val node2 = com.awp.samakaki.utils.Node("child")
//        val node3 = com.awp.samakaki.utils.Node(nodeText)
//        val node4 = com.awp.samakaki.utils.Node(nodeText)
//        val node5 = com.awp.samakaki.utils.Node(nodeText)
//        val node6 = com.awp.samakaki.utils.Node(nodeText)
//        val node8 = com.awp.samakaki.utils.Node(nodeText)
//        val node7 = com.awp.samakaki.utils.Node(nodeText)
//        val node9 = com.awp.samakaki.utils.Node(nodeText)
//        val node10 = com.awp.samakaki.utils.Node(nodeText)
//        val node11 = com.awp.samakaki.utils.Node(nodeText)
//        val node12 = com.awp.samakaki.utils.Node(nodeText)
//
//        val token = context?.let { SessionManager.getToken(it) }
//        familyTreeViewModel.findUserRelations("Bearer $token")
//        familyTreeViewModel.findUserRelations.observe(viewLifecycleOwner) {
//            when(it) {
//                is BaseResponse.Success -> {
//                    val relationData = it.data?.data?.relation
//                    Log.d("silsilah_keluarga", "hasilnya $relationData")
//                    val node1 = com.awp.samakaki.utils.Node(it.data?.data?.currentUser?.name!!)
//                    val node13 = com.awp.samakaki.utils.Node(it.data?.data?.relation!!.map { it.userRelated })
//                    graphDataTop.addNodes(node1)
//                }
//
//                is BaseResponse.Error -> textMessage(it.msg.toString())
//            }
//        }
//
//        return graphDataTop
//    }

}