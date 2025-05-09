#!/bin/bash

# Function to display help message
show_help() {
    echo "Usage: ./docker-run.sh [command]"
    echo ""
    echo "Commands:"
    echo "  build     Build the Docker images"
    echo "  start     Start the application"
    echo "  stop      Stop the application"
    echo "  restart   Restart the application"
    echo "  logs      Show application logs"
    echo "  clean     Remove containers and volumes"
    echo "  help      Show this help message"
}

# Function to build the application
build_app() {
    echo "Building the application..."
    docker-compose build
}

# Function to start the application
start_app() {
    echo "Starting the application..."
    docker-compose up -d
}

# Function to stop the application
stop_app() {
    echo "Stopping the application..."
    docker-compose down
}

# Function to restart the application
restart_app() {
    echo "Restarting the application..."
    docker-compose restart
}

# Function to show logs
show_logs() {
    echo "Showing application logs..."
    docker-compose logs -f
}

# Function to clean up
clean_up() {
    echo "Cleaning up..."
    docker-compose down -v
}

# Main script logic
case "$1" in
    "build")
        build_app
        ;;
    "start")
        start_app
        ;;
    "stop")
        stop_app
        ;;
    "restart")
        restart_app
        ;;
    "logs")
        show_logs
        ;;
    "clean")
        clean_up
        ;;
    "help"|"")
        show_help
        ;;
    *)
        echo "Unknown command: $1"
        show_help
        exit 1
        ;;
esac 