# 两个常用的混淆命令，注意：
# 一颗星表示只是保持该包下的类名，而子包下的类名还是会被混淆；
# 两颗星表示把本包和所含子包下的类名都保持；
# 声明com.akame.proguardfiee包下面的类名不进行混淆
#-keep class com.akame.proguardfiles.**
# 声明包下的所有的类名和方法都不进行混淆
-keep class com.akame.proguardfiles.**{*;}