version: 0.0
os: linux
files:
  - source:  /
    destination: /home/ubuntu/tikkle
    overwrite: yes

permissions:
  - object: /
    pattern: "**"
    owner: root
    group: root

hooks:
  AfterInstall:
    - location: scripts/deploy.sh
      timeout: 60
      runas: root