trait Logger {
    fn log(&self, message: String);
}

struct LogPrinter {
    message_id: u32
}

impl LogPrinter {
    fn new() -> LogPrinter {
        LogPrinter {
            message_id: 0
        }
    }
}
