package org.seekloud.esheepapi

import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import javafx.embed.swing.SwingFXUtils
import javafx.scene.{Group, Scene, SnapshotParameters}
import javafx.scene.canvas.Canvas
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import javafx.stage.Stage

import com.google.protobuf.ByteString

/**
	* Created by wangxicheng on 2018/12/13.
	*/
class CanvasToArray extends javafx.application.Application{
	
	override def start(primaryStage: Stage): Unit = {
		val group = new Group()
		val canvas = new Canvas(200, 200)
		val ctx = canvas.getGraphicsContext2D
		val scene = new Scene(group, 200, 200)
		group.getChildren.add(canvas)
		ctx.setFill(Color.web("rgba(128,255,0,0.5)"))
		ctx.fillRect(10, 0, 180, 200)
		
		val params = new SnapshotParameters
		params.setFill(Color.TRANSPARENT) //设置默认填充颜色，否则似乎alpha层不会适用于快照
		primaryStage.setScene(scene)
		primaryStage.sizeToScene()
		primaryStage.show()
		val width = canvas.getWidth.toInt
		val height = canvas.getHeight.toInt
		val writableImage = new WritableImage(width, height)
		val bufferedImage = new BufferedImage(width, width, 2)
		canvas.snapshot(params, writableImage) //从画布将图片转化为writableImage
		SwingFXUtils.fromFXImage(writableImage, bufferedImage) //将writableImage的数据倒入bufferedImage中
		
		/**
			* getRGB最后一个参数是指每行扫描的像素点数。如果想获得原图，直接填入width即可。
			* 填入其他值x，会导致图片每行只取前x个值。
			* 在一个Int内从高到低，每8位分别存的是A、R、G、B，0-255的值
			*/
		val argb = bufferedImage.getRGB(0, 0, width, height, null, 0, width)
		
		/**
			* allocate内表示buffer内最多存多少个Byte，byteBuffer本身有三个指针：
			* 	pos: 表示当下一个数据到来时应该被注入的位置，初始为0
			* 	lim: 表示在输出模式下时，输出的末尾位置，初始等于allocate里的大小。
			* 	cap: 表示buffer总长，初始等于allocate里的大小
			*
			* 保证线程安全的情况下，可以不每次重新开buffer，记得clear就行。
			*/
		val byteBuffer = ByteBuffer.allocate(4 * 200 * 200)
		// byteBuffer.clear()
		argb.foreach { e =>
			byteBuffer.putInt(e)
		}
		byteBuffer.flip() //翻转，修改lim为当前pos，pos置0
		val arrayOfByte = byteBuffer.array().take(byteBuffer.limit) //take前limit个Byte，去除Buffer内余下的多余数据
		print(arrayOfByte.toList)
		ByteString.copyFrom(arrayOfByte)
	}
}
