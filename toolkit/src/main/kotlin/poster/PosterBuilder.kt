import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*

class PosterBuilder(val drawer: Drawer) {

    val target = RenderTarget.active.let {
        renderTarget(it.width, it.height) {
            colorBuffer()
            depthBuffer()
        }
    }

    fun layer(blend: Filter? = null, post: List<Filter>?, builder: PosterBuilder.() -> Unit) {
        val rt = RenderTarget.active
        val layerTarget = renderTarget(rt.width, rt.height) {
            colorBuffer()
            depthBuffer()
        }

        drawer.isolatedWithTarget(layerTarget) {
            drawer.background(ColorRGBa.TRANSPARENT)
            builder()
        }

        val (tmpTargets, layerPost) = post?.let { filters ->
            val targets = List(Math.min(filters.size, 2)) {
                colorBuffer(rt.width, rt.height)
            }
            val result = filters.foldIndexed(layerTarget.colorBuffer(0)) { i, source, filter ->
                val target = targets[i % targets.size]
                filter.apply(source, target)
                target
            }
            Pair(targets, result)
        } ?: Pair(listOf(layerTarget.colorBuffer(0)), layerTarget.colorBuffer(0))

        if (blend == null) {
            drawer.isolatedWithTarget(rt) {
                drawer.image(layerPost, layerPost.bounds, drawer.bounds)
            }
        } else {
            blend.apply(arrayOf(rt.colorBuffer(0), layerPost), rt.colorBuffer(0))
        }

        if (post != null) {
            tmpTargets.forEach {
                it.destroy()
            }
        }

        layerTarget.colorBuffer(0).destroy()
        layerTarget.depthBuffer?.destroy()
        layerTarget.detachColorBuffers()
        layerTarget.detachDepthBuffer()
        layerTarget.destroy()
    }

    fun layer(blend: Filter? = null, post: Filter? = null, builder: PosterBuilder.() -> Unit) {
        layer(blend, post?.let { listOf(it) } ?: listOf(), builder)
    }

}


fun poster(drawer: Drawer, builder: PosterBuilder.() -> Unit) {

    val pb = PosterBuilder(drawer)
    drawer.withTarget(pb.target) {
        drawer.background(ColorRGBa.BLACK)
        pb.builder()
    }
    drawer.image(pb.target.colorBuffer(0), pb.target.colorBuffer(0).bounds, drawer.bounds)
}