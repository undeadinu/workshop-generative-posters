# Generative & Data Driven Posters

This is the code that accompanies the workshop

## Setup

 - Follow the [requirements](https://guide.openrndr.org/#/Tutorial_Start?id=requirements) and [prerequisites](https://guide.openrndr.org/#/Tutorial_Start?id=setting-up-prerequisites()) sections in the [OpenRNDR guide](https://guide.openrndr.org/).
 - Clone this repository and open it in IntelliJ.<br>
   *Hint*: You can do this from within IntelliJ if you're unfamiliar with [git](https://git-scm.com/).<br>
   Do the following:
   - Open IntelliJ
   - In the menubar, navigate: VCS / Checkout from Version Control / Git<br>
     Use URL: https://github.com/openrndr/workshop-generative-posters


## Project Structure
This project has three modules:

1. **toolkit**<br>
This module contains the tools that you'll be using during the workshop.

2. **examples**<br>
This module contains examples of how you can use the `toolkit`.

3. **workshop**<br>
This is an empty directory structure reserved for your own work.

## Getting Started
To get started, copy some file from `examples/src/main/kotlin/` to `workshop/src/main/kotlin` and start tweaking it.<br>
<span style="font-size: 15px">
[Also, checkout these examples](examples/README.md)
</span>


## Using the toolkit in a standalone project
If you'd like to use the toolkit in your own project, you can do the following:
- Clone the [OpenRNDR template project](https://github.com/openrndr/openrndr-gradle-template)
- Add the following to the project dependencies in `build.gradle`:<br>
```compile "com.github.openrndr.workshop-generative-posters:toolkit:-SNAPSHOT"```
- Replace `TemplateProgram.kt` with something from the `examples` module in this repository and run it.


# DOCS

## OpenRNDR 101

Here's how a very basic OpenRNDR program looks like:
```
import org.openrndr.*
import org.openrndr.color.ColorRGBa

class SimpleProgram: Program() {

    override fun setup(){
        println("Setup is called once at the start of the program.")
    }

    override fun draw() {
        // draw is called as many times a second as possible
        drawer.background(ColorRGBa.BLACK)
        drawer.fill = ColorRGBa.WHITE
        drawer.circle(width / 2.0, height / 2.0, Math.abs(Math.cos(seconds) * 300))
    }
}

fun main(args: Array<String>) {
    application(SimpleProgram(),
            configuration {
                width = 1280
                height = 720
            })
}
```

Your create a class which inherits from `Program` and override its two most important methods: `setup` and `draw`.
Setup is called once at the start of the program, then draw is called as many times a second as possible.
`setup` is for initializing the state of your program, while `draw` is for updating that state: creating animations.


#### Pro Tip ( ͡° ͜ʖ ͡°)
User the `FunctionDrawer` extension to define the draw function directly inside `setup`.
```
class SimpleProgram: Program() {

    override fun setup(){
        println("Setup is called once at the start of the program.")
        extend(FunctionDrawer{
            // draw is called as many times a second as possible
            drawer.background(ColorRGBa.BLACK)
            drawer.fill = ColorRGBa.WHITE
            drawer.circle(width / 2.0, height / 2.0, Math.abs(Math.cos(seconds) * 300))
        })
    }

}
```



## Toolkit

### Fonts
There are a number of hand picked fonts included in the toolkit.
Urls for these fonts are accessible through `org.openrndr.workshop.toolkit.typography.Fonts`.
Here is an example of using IBM Plex Mono Bold:
```
drawer.fontMap = FontImageMap.fromUrl(Fonts.IBMPlexMono_Bold, 32.0)
```

There is also a small sketch allowing you to preview available fonts using your own text at
`examples/main/kotlin/fonts/Fonts001.kt`. Use the left and right arrow keys to step through the different fonts, and use the scrollwheel of your mouse to increase or decrease the size at which the text is rendered at.
There is a function declared at the top of the sketch which determines the text being used for previewing the fonts.
Change this to
```
    val getText = { font: Font ->
        "your text"
    }
```
to use your own text for the specimens.
This can come in handy if you'd like to quickly test whether a specific font supports your alphabet.

