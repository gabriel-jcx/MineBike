## This is a forked repo of the MineBike Project which aims to upgrade to forge-1.12.2

# Setting up the workspace. 

  1. Unzip Forge Source/forge-1.12.2-14.23.5.2838-mdk.zip to the parent directory.
  2. Run the following command
     ```./gradlew setupDecompWorkspace --refresh-dependencies```
  
  3. For Eclipse workspace setup:
     ```
     ./gradlew ecplise
     ```
     Alternatively, if you wish to use IntelliJ:
     ```
     ./gradlew genIntellijRuns idea
     ```
  
  4. Then correctly import the project into your IDE.
  
