# GitHub actions to remove and create the repository

* Close Eclipse
* cd "{workspace_folder}"
* Copy "{projectName}" as "{projectName}_aux"
* Remove "lib", "target", ".git" and ".factoryPath" from "{projectName}_aux"
* Remove "{projectName}" in workspace
* Remove GitHub repository (in "Settings")
* Create new GitHub repository "Aliuken/{projectName}"
* git clone "{repositoryPath}"
* Copy files inside "{projectName}_aux" to the new "{projectName}"
* Remove folder "{projectName}_aux"
* cd "{projectName}"
* git add .
* git commit -m "first commit"
* git branch -M main
* git push