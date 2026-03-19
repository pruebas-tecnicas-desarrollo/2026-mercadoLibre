Write-Host "==> Stopping and removing containers (docker compose)..." -ForegroundColor Cyan
docker compose down --remove-orphans
Write-Host "Containers stopped and removed." -ForegroundColor Green

Write-Host "`n==> Removing unused images..." -ForegroundColor Cyan
docker image prune -a -f | Out-Null
Write-Host "Unused images removed." -ForegroundColor Green

Write-Host "`n==> Removing unused volumes..." -ForegroundColor Cyan
docker volume prune -f | Out-Null
Write-Host "Volumes removed." -ForegroundColor Green

Write-Host "`n==> Removing unused networks..." -ForegroundColor Cyan
docker network prune -f | Out-Null
Write-Host "Networks removed." -ForegroundColor Green

Write-Host "`n==> Building images (no cache)..." -ForegroundColor Blue
docker compose build --no-cache
Write-Host "Images built successfully." -ForegroundColor Green

Write-Host "`n==> Starting containers in detached mode..." -ForegroundColor Blue
docker compose up -d
Write-Host "Containers started successfully." -ForegroundColor Green

Write-Host "`n==> Showing live logs of the backend..." -ForegroundColor Magenta
docker compose logs -f backend