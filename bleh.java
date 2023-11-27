public static void GetMapping(int virtualPageNumber) {
        // Need to randomly generate number to determine where new mapping will go in TLB
        
        int randomNumber = randomNumberGenerator.nextInt(2);

        // Assign new mapping to TLB
        UserlandProcess.translationLookasideBuffer[randomNumber][0] = virtualPageNumber;
        
        // Kill if memory access is illegal
        if(kernel.GetPageMapping(virtualPageNumber) == null) {
            kernel.KillProcess();
        }
        
        // If there is no physical mapping to the virtual page
        if( kernel.GetPhyicalMapping(virtualPageNumber) == -1) {
            // Find a random process with a physical mapping to take page from
            KernelandProcess victimProcess = FindVictimProcess();

            // Get the first page with physical memory from victim
            int victimPage = 0;
            while(victimProcess.GetPhyicalPage(victimPage) == -1) {
                victimPage++;
            }

            // Victim physical page needs to be written to disk if not already written to disk
            if (victimProcess.GetPageMapping(victimPage).diskPage == -1) {
                // Calculate new disk page - put at end of swap file
                kernel.GetFakeFileSystem().Seek(swapFileDescriptor, diskPage * UserlandProcess.PAGE_SIZE);

                // Populate this array with page from physical memory from victims physical page
                byte[] pageData = new byte[UserlandProcess.PAGE_SIZE];

                // Loop over memory byte by byte copying physical page to array to be written to disk
                for(int i = victimProcess.GetPhyicalPage(victimPage) * UserlandProcess.PAGE_SIZE, j = 0; 
                i < (victimProcess.GetPhyicalPage(victimPage) * UserlandProcess.PAGE_SIZE) + UserlandProcess.PAGE_SIZE; 
                i++, j++) {
                    // Read bytes from memory
                    pageData[j] = UserlandProcess.memory[i];
                }

                // Write physical page bytes to disk
                kernel.GetFakeFileSystem().Seek(swapFileDescriptor, diskPage * UserlandProcess.PAGE_SIZE);
                // kernel.GetFakeFileSystem().Write(swapFileDescriptor, new byte[UserlandProcess.PAGE_SIZE]);
                kernel.GetFakeFileSystem().Write(swapFileDescriptor, pageData);

                // Set the disk mapping of the victim
                victimProcess.GetPageMapping(victimPage).diskPage = diskPage;
                diskPage++;
            }

            // Swap physical pages of current and victim process - unset victim physical page indicating no mapping
            kernel.GetPageMapping(virtualPageNumber).physicalPage = victimProcess.GetPageMapping(victimPage).physicalPage;
            victimProcess.GetPageMapping(victimPage).physicalPage = -1;

            // If there is a disk mapping for the current virtual page, read it back to memory
            if (kernel.GetPageMapping(virtualPageNumber).diskPage != -1) {
                // Find the stored disk page in the swap file
                kernel.GetFakeFileSystem().Seek(swapFileDescriptor, kernel.GetPageMapping(virtualPageNumber).diskPage * UserlandProcess.PAGE_SIZE);
                
                // Read the stored page from the swap file
                byte[] diskPageData = new byte[UserlandProcess.PAGE_SIZE];
                diskPageData = kernel.GetFakeFileSystem().Read(swapFileDescriptor, diskPageData.length);

                // Write the stored page to physical memory
                for(int i = kernel.GetPhyicalMapping(virtualPageNumber) * UserlandProcess.PAGE_SIZE, j = 0; 
                i < (kernel.GetPhyicalMapping(virtualPageNumber) * UserlandProcess.PAGE_SIZE) + UserlandProcess.PAGE_SIZE; 
                i++, j++) {
                    UserlandProcess.memory[i] = 0;
                    UserlandProcess.memory[i] = diskPageData[j];
                }
            }
            // If there is nothing to overwrite the bytes with, set it equal to 0
            else {
                // Wipe physical page before using physical page
                for(int i = kernel.GetPhyicalMapping(virtualPageNumber) * UserlandProcess.PAGE_SIZE; 
                i < (kernel.GetPhyicalMapping(virtualPageNumber) * UserlandProcess.PAGE_SIZE) + UserlandProcess.PAGE_SIZE; 
                i++) {
                    // Set physical bytes to 0
                    UserlandProcess.memory[i] = 0;
                }
            }
        }

        // Put mapping into TLB for caller process to reference
        UserlandProcess.translationLookasideBuffer[randomNumber][1] = kernel.GetPhyicalMapping(virtualPageNumber);
    }