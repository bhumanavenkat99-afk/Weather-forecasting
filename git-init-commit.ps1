param(
    [string]$RemoteUrl = $null,
    [string]$Message = "Initial commit: weather-forecasting project"
)

function ExitWith($code, $msg) {
    Write-Host $msg
    exit $code
}

# Check for git
try {
    git --version > $null 2>&1
} catch {
    ExitWith 1 "Git is not installed or not available in PATH. Install Git and re-run this script."
}

Write-Host "Initializing git repository..."
if (-not (Test-Path .git)) {
    git init
} else {
    Write-Host ".git already exists — skipping git init"
}

Write-Host "Adding files..."
git add .

Write-Host "Creating commit..."
# If there are no staged changes, git commit will fail; handle gracefully
$commitResult = git commit -m "$Message" 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "Commit command returned:"
    Write-Host $commitResult
    Write-Host "If no changes were staged, this may be ok."
} else {
    Write-Host "Commit created."
}

Write-Host "Renaming branch to 'main' (if applicable)..."
# Create or move to main
try {
    git branch -M main 2>$null
} catch {
    # ignore
}

if ($RemoteUrl) {
    Write-Host "Adding remote origin: $RemoteUrl"
    # If remote exists, update it
    $existing = git remote | Where-Object { $_ -eq 'origin' }
    if ($existing) {
        git remote remove origin
    }
    git remote add origin $RemoteUrl
    Write-Host "Pushing to remote origin main..."
    git push -u origin main
    if ($LASTEXITCODE -ne 0) {
        ExitWith 1 "Push failed. Check remote URL, authentication, and network."
    }
    Write-Host "Push completed."
} else {
    Write-Host "No remote URL provided. Local commit created. To push later, run: git remote add origin <URL> && git push -u origin main"
}

Write-Host "Done."