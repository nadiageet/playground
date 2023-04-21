import {QueryClient} from "react-query";

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            refetchOnMount: "always",
        }
    }
});


export {queryClient};