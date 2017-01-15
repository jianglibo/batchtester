Param([parameter(Mandatory=$true)]$folderToWatch)

# $folderToWatch = 'c:\scripts\test' # Enter the root path you want to monitor.
$filter = '*.*'  # You can enter a wildcard filter here.

# In the following line, you can change 'IncludeSubdirectories to $true if required.                          
# $fsw = New-Object IO.FileSystemWatcher $folderToWatch, $filter -Property @{IncludeSubdirectories = $false;NotifyFilter = [IO.NotifyFilters]'FileName, LastWrite'}

$watcher = New-Object System.IO.FileSystemWatcher
$watcher.Path = $folderToWatch
$watcher.Filter = '*.*'  # whatever you need
$watcher.IncludeSubDirectories = $true  # if needed
$watcher.EnableRaisingEvents = $true

# Here, all three events are registerd.  You need only subscribe to events that you need:

Register-ObjectEvent -InputObject $watcher -EventName Changed -Action {
    Write-Host "The file '$name' was $changeType at $timeStamp" -fore green
}

Start-Sleep -Seconds 10
# Register-ObjectEvent $watcher  Created -SourceIdentifier FileCreated -Action {
#     $name = $Event.SourceEventArgs.Name
#     $changeType = $Event.SourceEventArgs.ChangeType
#     $timeStamp = $Event.TimeGenerated
#     Write-Host "The file '$name' was $changeType at $timeStamp" -fore green
# }
# # Out-File -FilePath c:\scripts\filechange\outlog.txt -Append -InputObject "The file '$name' was $changeType at $timeStamp"}

# Register-ObjectEvent $fsw Deleted -SourceIdentifier FileDeleted -Action {
#     $name = $Event.SourceEventArgs.Name
#     $changeType = $Event.SourceEventArgs.ChangeType
#     $timeStamp = $Event.TimeGenerated
#     Write-Host "The file '$name' was $changeType at $timeStamp" -fore red
# }
# # Out-File -FilePath c:\scripts\filechange\outlog.txt -Append -InputObject "The file '$name' was $changeType at $timeStamp"}

# Register-ObjectEvent $fsw Changed -SourceIdentifier FileChanged -Action {
#     $name = $Event.SourceEventArgs.Name
#     $changeType = $Event.SourceEventArgs.ChangeType
#     $timeStamp = $Event.TimeGenerated
#     Write-Host "The file '$name' was $changeType at $timeStamp" -fore white
# }
# Out-File -FilePath c:\scripts\filechange\outlog.txt -Append -InputObject "The file '$name' was $changeType at $timeStamp"}

# To stop the monitoring, run the following commands:
# Unregister-Event FileDeleted
# Unregister-Event FileCreated
# Unregister-Event FileChanged



# To watch changes in all files, set the Filter property to an empty string (""). To watch a specific file, set the Filter property to the file name. For example, to watch for changes in the file MyDoc.txt, set the Filter property to "MyDoc.txt". You can also watch for changes in a certain type of file. For example, to watch for changes in any text files, set the Filter property to "*.txt". Use of multiple filters such as "*.txt|*.doc" is not supported.
# The Filter property can be changed after the FileSystemWatcher object has started receiving events.
# For more information about filtering out unwanted notifications, see the NotifyFilter, IncludeSubdirectories, and InternalBufferSize properties.
# Filter accepts wildcards for matching files, as shown in the following examples.

# using System;
# using System.IO;
# using System.Security.Permissions;

# public class Watcher
# {

#     public static void Main()
#     {
#     Run();

#     }

#     [PermissionSet(SecurityAction.Demand, Name="FullTrust")]
#     public static void Run()
#     {
#         string[] args = System.Environment.GetCommandLineArgs();

#         // If a directory is not specified, exit program.
#         if(args.Length != 2)
#         {
#             // Display the proper way to call the program.
#             Console.WriteLine("Usage: Watcher.exe (directory)");
#             return;
#         }

#         // Create a new FileSystemWatcher and set its properties.
#         FileSystemWatcher watcher = new FileSystemWatcher();
#         watcher.Path = args[1];
#         /* Watch for changes in LastAccess and LastWrite times, and
#            the renaming of files or directories. */
#         watcher.NotifyFilter = NotifyFilters.LastAccess | NotifyFilters.LastWrite
#            | NotifyFilters.FileName | NotifyFilters.DirectoryName;
#         // Only watch text files.
#         watcher.Filter = "*.txt";

#         // Add event handlers.
#         watcher.Changed += new FileSystemEventHandler(OnChanged);
#         watcher.Created += new FileSystemEventHandler(OnChanged);
#         watcher.Deleted += new FileSystemEventHandler(OnChanged);
#         watcher.Renamed += new RenamedEventHandler(OnRenamed);

#         // Begin watching.
#         watcher.EnableRaisingEvents = true;

#         // Wait for the user to quit the program.
#         Console.WriteLine("Press \'q\' to quit the sample.");
#         while(Console.Read()!='q');
#     }

#     // Define the event handlers.
#     private static void OnChanged(object source, FileSystemEventArgs e)
#     {
#         // Specify what is done when a file is changed, created, or deleted.
#        Console.WriteLine("File: " +  e.FullPath + " " + e.ChangeType);
#     }

#     private static void OnRenamed(object source, RenamedEventArgs e)
#     {
#         // Specify what is done when a file is renamed.
#         Console.WriteLine("File: {0} renamed to {1}", e.OldFullPath, e.FullPath);
#     }
# }
