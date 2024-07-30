# Git commands

## 1. Commands to create branch

```cmd
cd /path/to/project
git checkout -b feature/aliuken
git push -u origin feature/aliuken
```

## 2. Commands to upload code

```cmd
git checkout feature/aliuken
git add .
git commit -m "upload description"
git push
------------------------------------------------------
git checkout prerelease
git pull
git merge feature/aliuken
git push
------------------------------------------------------
git checkout develop
git pull
git merge prerelease
git push
------------------------------------------------------
git checkout main
git pull
git merge develop
git push
------------------------------------------------------
git checkout feature/aliuken
```

## 3. Commands to download code

```cmd
git checkout main
git pull
------------------------------------------------------
git checkout develop
git pull
git merge main
git push
------------------------------------------------------
git checkout prerelease
git pull
git merge develop
git push
------------------------------------------------------
git checkout feature/aliuken
git pull
git merge prerelease
git push
```

## 4. Upload code in one step

```cmd
git checkout feature/aliuken && git add . && git commit -m "upload description" && git push
------------------------------------------------------
git checkout prerelease && git pull && git merge feature/aliuken && git push
------------------------------------------------------
git checkout develop && git pull && git merge prerelease && git push
------------------------------------------------------
git checkout main && git pull && git merge develop && git push
------------------------------------------------------
git checkout feature/aliuken
```