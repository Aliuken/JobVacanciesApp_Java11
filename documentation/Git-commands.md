# Git commands

## 1. Commands to create branch

```shell
cd /path/to/project
git checkout -b feature/aliuken
git push -u origin feature/aliuken
```

## 2. Commands to upload code

```shell
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

```shell
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

```shell
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