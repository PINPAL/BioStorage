import os

# Define the JSON content to write into each .json file
json_content = '''{
    "type": "crafting_shapeless",
    "ingredients": [ { "item": "minecraft:air" } ],
    "result": { "item": "minecraft:barrier" }
}'''

def find_files(directory, files=[]):
    for entry in os.scandir(directory):
        if len(files) >= 5:
            break
        if entry.is_dir(follow_symlinks=False):
            find_files(entry.path, files)
        elif entry.is_file():
            files.append(entry.path)
    return files

def find_and_modify_json_files(directory):
    for entry in os.scandir(directory):
        if entry.is_dir(follow_symlinks=False):
            find_and_modify_json_files(entry.path)
        elif entry.is_file() and entry.name.endswith('.json'):
            modify_json_file(entry.path)

def modify_json_file(file_path):
    with open(file_path, 'w') as file:
        file.write(json_content)
    print(f'Modified: {file_path}')

# Starting directory is the script's location
start_directory = os.path.dirname(os.path.abspath(__file__))

# Find the first 5 files for confirmation prompt
first_five_files = find_files(start_directory)

# Print current directory and first 5 files
print(f"Current directory: {start_directory}")
print("First 5 files found:")
for file in first_five_files:
    print(file)

# Confirmation prompt
confirmation = input("Do you want to proceed with modifying all .json files in this directory and its subdirectories? (y/n): ")

if confirmation.lower() == 'y':
    find_and_modify_json_files(start_directory)
    print("Modification completed.")
else:
    print("Operation cancelled.")
