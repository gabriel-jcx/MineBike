This is a forked repo of the MineBike Project which aims to upgrade to forge-1.12.2

## Setting up the workspace. 

  1. Unzip Forge Source/forge-1.12.2-14.23.5.2838-mdk.zip to the parent directory.
  2. Run the following command
     ```./gradlew setupDecompWorkspace --refresh-dependencies```
  
### Setting up the environment in Eclipse
     For Eclipse workspace setup:
     ```
     ./gradlew eclipse
     ```

### Setting up the environment in IntelliJ     
     Alternatively, if you wish to use IntelliJ:
     ```
     ./gradlew genIntellijRuns
     ```
     then run:
     ```
     ./gradlew idea
     ```
     The above command would generate a .ipr intelliJ project file for you to import, you 
  
  4. Then correctly import the project into your IDE. If you import the project using Inteillij, you can choose the ```build.gradle``` file, which would then setup your Module in the MinecraftMod.main.
  
  5. Use ```GradleStart``` to start and test the client side.
