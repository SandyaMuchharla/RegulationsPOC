# Background

You have been approached by an SME within a Regulatory team which are already underway with their project.
The project team has to validate data within various report files to ensure consistency with the Regulator requirements.
A cut-down sample of the current version report files (to verify) and requirements have been provided by the team.
The requirements are not finalised and will be subject to change during the course of the project.
The team would like to understand how best to automate this process, given the tests will need to be run numerous times going forward.
Your role is to create a simple Proof of Concept, for two of the tests, which will potentially form the basis of a regression framework that the team can build upon themselves.


# Test Instructions 

* Using any IDE, language and libraries of your choice, please analyse and implement the below two tests.
* Report any potential bugs or issues found based on the current report files and requirements provided by the team.
* Please state any assumptions you feel are required / justified with your solution.
* You can use any available resource to complete this task (e.g. Google / ChatGPT)  and there is no time restriction.

* Please provide as your response:
	
	1. Code
	2. Solution/Repo screenshot
	3. Pass/Fail test status for both tests.
	4. Any potential bugs/issues found with necessary details.
	5. A brief explanation of your solution to a non-technical manager/stakeholder.



# Repo Structure 

Freeformat/
│
├── evidence/
│   └── ...  # Repository screenshots, test evidence, reported bugs, and explanations to be provided by candidate.
│
├── files/
│   ├── y_*  # Test scope limited to these files. All others are to be ignored.
│   └── ...
│
├── requirements/
│   └── Data Fields_New.xlsx  # Excel file containing test data fields and definitions.
│
└── tests/
    └── ...  # Location test scripts and other test-related files to be provided by candidate.
	
	

# Test Scenario 1 - No column can be reported twice.

1.	All underlying files (within scope) must be verified to have no duplicate columns PER file.


# Test Scenario 2 - Validate [Yes/No] Type.

1. .\requirements\Data Fields_New.xlsx contains the data definitions for files under test.
2. Ensure relevant columns within the report files meet the [Yes/No] datatype criteria.
3. Consider how this test could be impacted by new versions of both report files and changes to requirements.

