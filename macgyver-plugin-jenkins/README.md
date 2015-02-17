# jenkins plugin

This plugin provides basic Jenkins API access from within MacGyver.


## Programmatic Config

```java

import io.macgyver.plugin.jenkins.*;

		JenkinsClient c = new JenkinsClientBuilder()
				.url("https://jenkins.example.com")
				.credentials("myusername", "tokenorpassword")
				.build();
```