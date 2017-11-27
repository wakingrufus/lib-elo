#!/usr/bin/env bash
echo Enter signing key id
read key_id
echo Enter signing key password
read key_pass
gpg2 --export-secret-key $key_id > secring.gpg
echo Enter ossrh Username
read ossrh_username
echo Enter ossrh Password
read ossrh_password
gradle -PossrhUsername=$ossrh_username -PossrhPassword=$ossrh_password -Psigning.keyId=$key_id -Psigning.password=$key_pass signArchives uploadArchives closeAndReleaseRepository